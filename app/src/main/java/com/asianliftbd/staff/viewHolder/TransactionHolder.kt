package com.asianliftbd.staff.viewHolder

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.asianliftbd.staff.R
import com.asianliftbd.staff.Utils.amountFormatTextView

class TransactionHolder(var v: View) : RecyclerView.ViewHolder(v) {
    fun setDetails(date: String, title: String, details: String?, amount: Int) {
        val transactionLayout = v.findViewById<RelativeLayout>(R.id.transactionLayout)

        val dateTextView = v.findViewById<TextView>(R.id.date)
        val titleTextView = v.findViewById<TextView>(R.id.title)
        val detailsTextView = v.findViewById<TextView>(R.id.details)
        val amountTextView = v.findViewById<TextView>(R.id.amount)

        dateTextView.text = date
        titleTextView.text = title

        amountFormatTextView(amountTextView, amount)
        if (amount >= 0) {
            transactionLayout.setBackgroundResource(R.drawable.gradient_linear_green)
        } else {
            transactionLayout.setBackgroundResource(R.drawable.gradient_linear_red)
        }

        if (details.isNullOrEmpty()) detailsTextView.visibility = View.GONE
        else {
            detailsTextView.text = details
            detailsTextView.visibility = View.VISIBLE
        }
    }
}