package com.asianliftbd.staff.activity

import android.content.Intent
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asianliftbd.staff.R
import com.asianliftbd.staff.Utils.amountFormatTextView
import com.asianliftbd.staff.databinding.ActivityTransactionBinding
import com.asianliftbd.staff.viewHolder.TransactionHolder
import com.asianliftbd.staff.model.TransactionModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class Transaction : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding

    private lateinit var transactionLayout: RelativeLayout
    private var recyclerView: RecyclerView? = null
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var totalBalanceValue: TextView
    private lateinit var emptyTextView: TextView

    private val uid: String = FirebaseAuth.getInstance().uid.toString()
    private var i = 0
    private var conveyance = 0

    private val ref = FirebaseDatabase.getInstance().reference
    private val refTransaction = ref.child("transaction/staff/$uid")
    private var refTransactionBalance = ref.child("balance/staff/$uid/value")
    private var refConveyanceBalance = ref.child("balance/conveyance/$uid/value")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        transactionLayout = binding.transactionLayout
        recyclerView = binding.recyclerView
        totalBalanceValue = binding.totalBalanceValue
        emptyTextView = binding.emptyTextView

        recyclerView?.run {
            layoutManager = linearLayoutManager
        }

        ref.child("balance/staff/$uid/date").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                supportActionBar?.subtitle = if (d.exists()) String.format("Updated on %s", d.getValue<String>())
                else ""
            }
            override fun onCancelled(e: DatabaseError) {}
        })

        val conveyanceLayout = binding.totalConveyanceLayout
        val conveyanceAmount = binding.totalConveyanceValue

        refConveyanceBalance.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                if (!d.exists()) conveyance = 0
                else conveyance = d.getValue(Int::class.java) ?: 0

                if (conveyance == 0) conveyanceLayout.visibility = View.GONE
                else {
                    amountFormatTextView(conveyanceAmount, conveyance)
                    conveyanceLayout.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(e: DatabaseError) {}
        })

        conveyanceLayout.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            startActivity(Intent(applicationContext, Conveyance::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()
        refTransaction.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                i = 0

                if (!d.exists()) {
                    if (conveyance == 0) {
                        transactionLayout.visibility = View.GONE
                    } else {
                        transactionLayout.visibility = View.VISIBLE
                        recyclerView?.visibility = View.GONE
                        emptyTextView.text = "Only conveyance found"
                    }
                    emptyTextView.visibility = View.VISIBLE
                    refTransactionBalance.setValue(0)
                } else {
                    emptyTextView.visibility = View.GONE
                    transactionLayout.visibility = View.VISIBLE

                    val options = FirebaseRecyclerOptions.Builder<TransactionModel>()
                        .setQuery(refTransaction, TransactionModel::class.java)
                        .build()

                    val recyclerAdapter: FirebaseRecyclerAdapter<TransactionModel, TransactionHolder> = object : FirebaseRecyclerAdapter<TransactionModel, TransactionHolder>(options) {
                        override fun onBindViewHolder(vh: TransactionHolder, position: Int, u: TransactionModel) {
                            i += u.amount
                            refTransactionBalance.setValue(i)
                            amountFormatTextView(totalBalanceValue, i)
                            vh.setDetails(u.date, u.title, u.details, u.amount)
                        }

                        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
                            val view = LayoutInflater.from(parent.context)
                                .inflate(R.layout.model_transaction, parent, false)
                            return TransactionHolder(view)
                        }
                    }

                    recyclerView?.adapter = recyclerAdapter
                    recyclerAdapter.startListening()
                }
            }
            override fun onCancelled(e: DatabaseError) {}
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}