package com.asianliftbd.staff.viewHolder

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.asianliftbd.staff.activity.CallbackProject
import com.asianliftbd.staff.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class CallbackTotalHolder(private var v: View) : RecyclerView.ViewHolder(v) {
    fun setDetails(ctx: Context, name: String, ref: DatabaseReference) {
        val nameTextView = v.findViewById<TextView>(R.id.name)
        val dateTextView = v.findViewById<TextView>(R.id.date)
        val valueTextView = v.findViewById<TextView>(R.id.value)
        val layout = v.findViewById<LinearLayout>(R.id.layout)

        nameTextView.text = name
        dateTextView.visibility = View.GONE

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                valueTextView.text = snapshot.childrenCount.toString()
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        layout.setOnClickListener {
            val i = Intent(ctx, CallbackProject::class.java)
            i.putExtra("project", name)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(i)
        }
    }
}