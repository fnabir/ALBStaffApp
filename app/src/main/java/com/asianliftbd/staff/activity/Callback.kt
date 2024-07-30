package com.asianliftbd.staff.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asianliftbd.staff.R
import com.asianliftbd.staff.databinding.ActivityCallbackBinding
import com.asianliftbd.staff.model.CallbackTotalModel
import com.asianliftbd.staff.viewHolder.CallbackTotalHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Callback : AppCompatActivity() {
    private lateinit var binding: ActivityCallbackBinding

    private lateinit var recyclerView: RecyclerView
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var emptyTextView: TextView

    private val ref = FirebaseDatabase.getInstance().getReference("callback")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCallbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        recyclerView = binding.recyclerView
        emptyTextView = binding.emptyTextView

        recyclerView.run {
            layoutManager = linearLayoutManager
        }
    }

    public override fun onStart() {
        super.onStart()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                if (!d.exists()) {
                    recyclerView.visibility = View.GONE
                    emptyTextView.visibility = View.VISIBLE
                } else {
                    emptyTextView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    val options = FirebaseRecyclerOptions.Builder<CallbackTotalModel>()
                        .setQuery(ref, CallbackTotalModel::class.java)
                        .build()

                    val recyclerAdapter: FirebaseRecyclerAdapter<CallbackTotalModel, CallbackTotalHolder> =
                        object : FirebaseRecyclerAdapter<CallbackTotalModel, CallbackTotalHolder>(options) {
                            override fun onBindViewHolder(
                                vh: CallbackTotalHolder,
                                position: Int,
                                ut: CallbackTotalModel
                            ) {
                                vh.setDetails(applicationContext, getRef(position).key.toString(), getRef(position))
                            }

                            override fun onCreateViewHolder(
                                parent: ViewGroup,
                                viewType: Int
                            ): CallbackTotalHolder {
                                val view = LayoutInflater.from(parent.context)
                                    .inflate(R.layout.model_balance, parent, false)
                                return CallbackTotalHolder(view)
                            }
                        }
                    recyclerView.adapter = recyclerAdapter
                    recyclerAdapter.startListening()
                }
            }

            override fun onCancelled(e: DatabaseError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_add -> {
                startActivity(Intent(applicationContext, CallbackNew::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}