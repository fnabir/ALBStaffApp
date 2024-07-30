package com.asianliftbd.staff.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asianliftbd.staff.R
import com.asianliftbd.staff.databinding.ActivityCallbackProjectBinding
import com.asianliftbd.staff.model.CallbackModel
import com.asianliftbd.staff.viewHolder.CallbackHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CallbackProject : AppCompatActivity() {
    private lateinit var binding: ActivityCallbackProjectBinding

    private lateinit var recyclerView: RecyclerView
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var emptyTextView: TextView

    private lateinit var project: String
    private lateinit var ref: DatabaseReference

    private val uid = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallbackProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val extras = intent.extras
        if (extras != null) {
            project = extras.getString("project").toString()
            supportActionBar?.title = project
            ref = FirebaseDatabase.getInstance().getReference("callback/$project")
        } else {
            finish()
            Toast.makeText(this, "Error Occurred! Try again", Toast.LENGTH_SHORT).show()
        }

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
                    val options = FirebaseRecyclerOptions.Builder<CallbackModel>()
                        .setQuery(ref.orderByChild("dbdate"), CallbackModel::class.java)
                        .build()
                    val recyclerAdapter: FirebaseRecyclerAdapter<CallbackModel, CallbackHolder> =
                        object : FirebaseRecyclerAdapter<CallbackModel, CallbackHolder>(options) {
                            override fun onBindViewHolder(
                                vh: CallbackHolder,
                                position: Int,
                                ut: CallbackModel
                            ) {
                                vh.setDetails(applicationContext, ut.name, ut.details, ut.date, project, getRef(position).key.toString(), uid, ut.uid)
                            }

                            override fun onCreateViewHolder(
                                parent: ViewGroup,
                                viewType: Int
                            ): CallbackHolder {
                                val view = LayoutInflater.from(parent.context)
                                    .inflate(R.layout.model_transaction, parent, false)
                                return CallbackHolder(view)
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