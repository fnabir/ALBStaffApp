package com.asianliftbd.staff.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asianliftbd.staff.R
import com.asianliftbd.staff.Utils.amountFormatTextView
import com.asianliftbd.staff.databinding.ActivityConveyanceBinding
import com.asianliftbd.staff.model.TransactionModel
import com.asianliftbd.staff.viewHolder.TransactionHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class Conveyance : AppCompatActivity() {
    private lateinit var binding: ActivityConveyanceBinding

    private lateinit var transactionLayout: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var totalBalanceValue: TextView
    private lateinit var emptyTextView: TextView

    private val uid = FirebaseAuth.getInstance().currentUser!!.uid
    private var i = 0

    private val ref = FirebaseDatabase.getInstance().reference
    private var refTransaction = ref.child("transaction/conveyance/$uid")
    private var refTotalBalance = ref.child("balance/conveyance/$uid")

    private var recyclerAdapter: FirebaseRecyclerAdapter<TransactionModel, TransactionHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConveyanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        transactionLayout = binding.transactionLayout
        recyclerView = binding.recyclerView
        totalBalanceValue = binding.totalBalanceValue
        emptyTextView = binding.emptyTextView

        recyclerView.run {
            layoutManager = linearLayoutManager
        }

        refTotalBalance?.child("date")?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                if (d.exists()) supportActionBar?.subtitle = String.format("Updated on %s", d.getValue<String>())
            }
            override fun onCancelled(e: DatabaseError) {}
        })
    }

    public override fun onStart() {
        super.onStart()
        refTransaction.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                i = 0

                if (!d.exists()) {
                    transactionLayout.visibility = View.GONE
                    emptyTextView.visibility = View.VISIBLE
                    refTotalBalance.child("value")?.setValue(0)
                } else {
                    emptyTextView.visibility = View.GONE
                    transactionLayout.visibility = View.VISIBLE

                    val options = FirebaseRecyclerOptions.Builder<TransactionModel>()
                        .setQuery(refTransaction, TransactionModel::class.java)
                        .build()

                    recyclerAdapter = object : FirebaseRecyclerAdapter<TransactionModel, TransactionHolder>(options) {
                        override fun onBindViewHolder(vh: TransactionHolder, position: Int, u: TransactionModel) {
                            i += u.amount
                            refTotalBalance.child("value").setValue(i)
                            amountFormatTextView(totalBalanceValue, i)
                            vh.setDetails(u.date, u.title, u.details, u.amount)
                        }

                        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
                            val view = LayoutInflater.from(parent.context)
                                .inflate(R.layout.model_balance, parent, false)
                            return TransactionHolder(view)
                        }
                    }

                    recyclerView.adapter = recyclerAdapter
                    recyclerAdapter?.startListening()
                }
            }
            override fun onCancelled(e: DatabaseError) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerAdapter?.stopListening()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}