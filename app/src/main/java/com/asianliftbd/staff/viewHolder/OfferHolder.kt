package com.asianliftbd.staff.viewHolder

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.asianliftbd.staff.R
import com.asianliftbd.staff.activity.OfferNew
import com.google.firebase.auth.FirebaseAuth

class OfferHolder(private val v: View) : RecyclerView.ViewHolder(v) {
    fun setDetails(
        ctx: Context,
        name: String, uid: String, address: String?,
        projectType: String?, workType: String,
        unit: String?, floor: String?, shaft: String?, person: String?,
        note: String?, refer: String?, date: String?, key: String
    ) {
        val textViewName = v.findViewById<TextView>(R.id.name)
        val textViewAddress = v.findViewById<TextView>(R.id.address)
        val textViewType = v.findViewById<TextView>(R.id.type)
        val textViewFloor = v.findViewById<TextView>(R.id.floor)
        val textViewShaft = v.findViewById<TextView>(R.id.shaft)
        val textViewNote = v.findViewById<TextView>(R.id.note)
        val textViewRefer = v.findViewById<TextView>(R.id.refer)
        val m = v.findViewById<RelativeLayout>(R.id.m)

        textViewName.text = name
        textViewShaft.text = shaft
        textViewNote.text = note
        textViewRefer.text = String.format("%s | %s", refer, date)

        if (address?.isEmpty() == true || address == null) textViewAddress.visibility = View.GONE
        else textViewAddress.text = address

        if (workType.isEmpty() && (unit?.isEmpty() == true || unit == null)) textViewType.text = projectType
        else if (workType.isEmpty() && unit?.isNotEmpty() == true || unit == null) textViewType.text = String.format("%s > %s Unit(s)", projectType, unit)
        else if (workType.isNotEmpty() && unit.isEmpty()) textViewType.text = String.format("%s > %s", projectType, workType)
        else textViewType.text = String.format("%s > %s > %s Unit(s)", projectType, workType, unit)

        if (person?.isEmpty() == true && floor?.isEmpty() == true || floor == null) textViewFloor.visibility = View.GONE
        else if (floor.isEmpty() && person?.isNotEmpty() == true) textViewFloor.text = String.format("Person/Load: %s ", person)
        else if (person?.isEmpty() == true && floor.isNotEmpty()) textViewFloor.text = String.format("%s Floor", floor)
        else textViewFloor.text = String.format("%s Floor | Person/Load: %s", floor, person)

        if (shaft?.isEmpty() == true || shaft == null) textViewShaft.visibility = View.GONE
        else textViewShaft.text = shaft

        if (note?.isEmpty() == true || note == null) textViewNote.visibility = View.GONE
        else textViewNote.text = note

        m.setOnLongClickListener {
            if ((FirebaseAuth.getInstance().uid == uid)) {
                val i = Intent(ctx, OfferNew::class.java)
                i.putExtra("key", key)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ctx.startActivity(i)
            } else {
                Toast.makeText(ctx, "This offer is not submitted by you", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}