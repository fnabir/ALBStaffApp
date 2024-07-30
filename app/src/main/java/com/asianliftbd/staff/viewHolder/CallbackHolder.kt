package com.asianliftbd.staff.viewHolder

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.asianliftbd.staff.R
import com.asianliftbd.staff.activity.CallbackNew

class CallbackHolder(private val v: View) : RecyclerView.ViewHolder(v) {
    fun setDetails(ctx: Context,
                   name: String, details: String, date: String,
                   project: String, key: String,
                   currentUid: String, uid: String? = null) {
        val dateTextView = v.findViewById<TextView>(R.id.date)
        val titleTextView = v.findViewById<TextView>(R.id.title)
        val detailsTextView = v.findViewById<TextView>(R.id.details)
        val amountTextView = v.findViewById<TextView>(R.id.amount)
        val layout = v.findViewById<RelativeLayout>(R.id.transactionLayout)

        dateTextView.text = date
        titleTextView.text = name
        detailsTextView.text = details
        detailsTextView.visibility = View.VISIBLE
        amountTextView.visibility = View.GONE

        layout.setOnClickListener {
            if (uid == currentUid) {
                val i = Intent(ctx, CallbackNew::class.java)
                i.putExtra("key", key)
                i.putExtra("project", project)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ctx.startActivity(i)
            } else {
                Toast.makeText(ctx, "Not authorized to edit this record", Toast.LENGTH_SHORT).show()
            }
        }
    }
}