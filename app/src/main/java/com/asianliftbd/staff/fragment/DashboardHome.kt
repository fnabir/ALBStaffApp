package com.asianliftbd.staff.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.asianliftbd.staff.BuildConfig
import com.asianliftbd.staff.R
import com.asianliftbd.staff.Utils
import com.asianliftbd.staff.activity.AboutApp
import com.asianliftbd.staff.activity.Callback
import com.asianliftbd.staff.activity.Conveyance
import com.asianliftbd.staff.activity.EditProfile
import com.asianliftbd.staff.activity.Error
import com.asianliftbd.staff.activity.Offer
import com.asianliftbd.staff.activity.Transaction
import com.asianliftbd.staff.databinding.FragmentDashboardHomeBinding
import com.asianliftbd.staff.model.MessageModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.messaging.FirebaseMessaging
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DashboardHome : Fragment() {

    private val tag = "DashboardHome"

    private var _binding: FragmentDashboardHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var updateLayout: LinearLayout
    private lateinit var releaseDate: TextView

    private lateinit var messageLayout: LinearLayout
    private lateinit var messageTitle: TextView
    private lateinit var messageText: TextView

    private lateinit var transactionBalance: TextView
    private lateinit var transactionBalanceDate: TextView

    private lateinit var conveyanceBalance: TextView
    private lateinit var conveyanceBalanceDate: TextView

    private lateinit var offerButton: MaterialButton

    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.uid
    private val user = auth.currentUser
    private val ref = FirebaseDatabase.getInstance().reference
    private val refAppCheck = ref.child("app/staff")
    private val refMessage = ref.child("app/message/staff")
    private val refTransactionBalance = ref.child("balance/staff/$uid")
    private val refConveyanceBalance = ref.child("balance/conveyance/$uid")
    private val refOffer = ref.child("-offer")

    private val appCheckListener = object : ValueEventListener {
        override fun onDataChange(d: DataSnapshot) {
            if (d.child("code").exists()) {
                val newVersionCode =  d.child("code").getValue<Int>() ?: BuildConfig.VERSION_CODE

                if (newVersionCode > BuildConfig.VERSION_CODE) {
                    updateLayout.visibility = View.VISIBLE
                    val releaseDateText = "Released on ${d.child("date").getValue<String>()}"
                    releaseDate.text = releaseDateText
                } else updateLayout.visibility = View.GONE
            }
        }
        override fun onCancelled(e: DatabaseError) {}
    }

    private val messageListener = object : ValueEventListener {
        override fun onDataChange(d: DataSnapshot) {
            val u = d.getValue(MessageModel::class.java)
            if (u != null) {
                if (u.show){
                    messageLayout.visibility = View.VISIBLE
                    when (u.title) {
                        "Urgent" -> messageLayout.setBackgroundResource(R.drawable.btn_yellow)
                        "Update" -> messageLayout.setBackgroundResource(R.drawable.btn_green)
                        else -> messageLayout.setBackgroundResource(R.drawable.btn_blue_ripple)
                    }

                    if (u.title == null) messageTitle.visibility = View.GONE
                    else {
                        messageTitle.visibility = View.VISIBLE
                        messageTitle.text = u.title
                    }

                    val messageDatabaseText = u.text
                    messageText.text = messageDatabaseText.toString().replace("|","\n")
                } else messageLayout.visibility = View.GONE
            } else messageLayout.visibility = View.GONE
        }
        override fun onCancelled(e: DatabaseError) {
            Log.e("Message Database Error", e.toString())
        }
    }

    private val transactionBalanceListener = object : ValueEventListener {
        override fun onDataChange(d: DataSnapshot) {
            Utils.amountFormatTextView(transactionBalance, d.child("value").getValue<Int>() ?: 0)
            transactionBalanceDate.text = String.format("Updated on %s", d.child("date").value as String)
        }
        override fun onCancelled(e: DatabaseError) {}
    }

    private val conveyanceBalanceListener = object : ValueEventListener {
        override fun onDataChange(d: DataSnapshot) {
            Utils.amountFormatTextView(conveyanceBalance, d.child("value").getValue<Int>() ?: 0)
            conveyanceBalanceDate.text = String.format("Updated on %s", d.child("date").value as String)
        }
        override fun onCancelled(e: DatabaseError) {}
    }

    private val offerListener = object : ValueEventListener {
        override fun onDataChange(d: DataSnapshot) {
            val offerCount = d.childrenCount
            val offerButtonText = if (offerCount > 0) "Offer (${d.childrenCount})"
            else "Offer"
            offerButton.text = offerButtonText
        }
        override fun onCancelled(e: DatabaseError) {
            Log.e("Offer Database Error", e.toString())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val versionName = BuildConfig.VERSION_NAME
        val versionCode = BuildConfig.VERSION_CODE.toString()
        ref.child("info/user/$uid/version").setValue("Staff-$versionName($versionCode)")

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(tag, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            ref.child("info/user/$uid/token").setValue(token)
        })

        val userNameTextView = binding.name
        val userName = user!!.displayName

        refOffer.addValueEventListener(appCheckListener)

        if (userName.isNullOrEmpty()) {
            startActivity(Intent(activity, EditProfile::class.java))
        } else userNameTextView.text = String.format("Hello, %s", userName)

        val wish = root.findViewById<TextView>(R.id.wish)
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH", Locale.getDefault())
        val temp = sdf.format(calendar.timeInMillis).toInt()
        if ((temp < 6) || (temp > 22)) {
            wish.text = getString(R.string.good_night)
        } else if (temp < 12) {
            wish.text = getString(R.string.good_morning)
        } else if (temp < 17) {
            wish.text = getString(R.string.good_afternoon)
        } else {
            wish.text = getString(R.string.good_evening)
        }

        updateLayout = binding.updateLayout
        releaseDate = binding.releaseDate
        refAppCheck.addValueEventListener(appCheckListener)

        val downloadButton = binding.downloadButton
        downloadButton.setOnClickListener {
            Utils.singleClick()
            startActivity(Intent(activity, AboutApp::class.java))
        }

        messageLayout = binding.messageLayout
        messageTitle = binding.messageTitle
        messageText = binding.messageText
        refMessage.addListenerForSingleValueEvent(messageListener)

        transactionBalance = binding.transactionBalance
        transactionBalanceDate = binding.transactionBalanceDate

        conveyanceBalance = binding.conveyanceBalance
        conveyanceBalanceDate = binding.conveyanceBalanceDate

        refTransactionBalance.addValueEventListener(transactionBalanceListener)
        refConveyanceBalance.addValueEventListener(conveyanceBalanceListener)

        val transactionLayout = binding.transactionLayout
        transactionLayout.setOnClickListener {
            refTransactionBalance.removeEventListener(transactionBalanceListener)
            Utils.singleClick()
            startActivity(Intent(activity, Transaction::class.java))
        }

        val conveyanceLayout = binding.conveyanceLayout
        conveyanceLayout.setOnClickListener {
            refConveyanceBalance.removeEventListener(conveyanceBalanceListener)
            Utils.singleClick()
            startActivity(Intent(activity, Conveyance::class.java))
        }

        offerButton = binding.offerButton
        offerButton.setOnClickListener {
            refOffer.addValueEventListener(offerListener)
            Utils.singleClick()
            startActivity(Intent(activity, Offer::class.java))
        }

        val callbackButton = binding.callbackButton
        callbackButton.setOnClickListener {
            Utils.singleClick()
            startActivity(Intent(activity, Callback::class.java))
        }

        val errorLayout = binding.errorLayout
        errorLayout.setOnClickListener {
            Utils.singleClick()
            startActivity(Intent(activity, Error::class.java))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}