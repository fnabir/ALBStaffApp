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
import com.asianliftbd.staff.databinding.ActivityOfferBinding
import com.asianliftbd.staff.viewHolder.OfferHolder
import com.asianliftbd.staff.model.OfferModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Offer : AppCompatActivity() {
    private lateinit var binding: ActivityOfferBinding

    private lateinit var recyclerView: RecyclerView
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var emptyTextView: TextView

    private var ref = FirebaseDatabase.getInstance().reference.child("-offer")

    private val options = FirebaseRecyclerOptions.Builder<OfferModel>()
        .setQuery(ref, OfferModel::class.java)
        .build()

    private val recyclerAdapter: FirebaseRecyclerAdapter<OfferModel, OfferHolder> =
        object : FirebaseRecyclerAdapter<OfferModel, OfferHolder>(options) {
            override fun onBindViewHolder(vh: OfferHolder, position: Int, ut: OfferModel) {
                vh.setDetails(
                    applicationContext, ut.name, ut.uid, ut.address,
                    ut.ptype, ut.wtype, ut.unit, ut.floor, ut.shaft, ut.person, ut.note, ut.refer, ut.date, getRef(position).key.toString()
                )
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): OfferHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.model_offer, parent, false)
                return OfferHolder(view)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOfferBinding.inflate(layoutInflater)
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

                    recyclerView.adapter = recyclerAdapter
                    recyclerAdapter.startListening()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_add -> {
                startActivity(Intent(applicationContext, OfferNew::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}