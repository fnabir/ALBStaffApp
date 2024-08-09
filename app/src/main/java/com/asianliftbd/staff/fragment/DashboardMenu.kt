package com.asianliftbd.staff.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.asianliftbd.staff.BuildConfig
import com.asianliftbd.staff.R
import com.asianliftbd.staff.Utils
import com.asianliftbd.staff.activity.AboutApp
import com.asianliftbd.staff.activity.EditProfile
import com.asianliftbd.staff.activity.Login
import com.asianliftbd.staff.activity.PasswordChange
import com.asianliftbd.staff.databinding.FragmentDashboardMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardMenu : Fragment() {

    private var _binding: FragmentDashboardMenuBinding? = null
    private val binding get() = _binding!!

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val user = firebaseAuth.currentUser
    private val uid = firebaseAuth.uid

    private val ref = FirebaseDatabase.getInstance().reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val userName = binding.name
        userName.text = user!!.displayName

        val userPhone = binding.phone
        ref.child("info/user/$uid/phone").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                if (d.exists()) userPhone.text = d.value.toString()
                else userPhone.visibility = View.GONE
            }
            override fun onCancelled(e: DatabaseError) {}
        })

        val userMail = binding.mail
        userMail.text = user.email

        val version = binding.version
        val versionText = "ALB Admin App - ${BuildConfig.VERSION_NAME}"
        version.text = versionText

        val website = root.findViewById<TextView>(R.id.website)
        website.setOnClickListener {
            Utils.singleClick()
            val url = "https://asianliftbd.com"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        val editProfile = binding.editProfile
        editProfile.setOnClickListener {
            Utils.singleClick()
            startActivity(Intent(activity, EditProfile::class.java))
        }

        val change = binding.changePassword
        change.setOnClickListener {
            Utils.singleClick()
            startActivity(Intent(activity, PasswordChange::class.java))
        }

        val facebook = binding.facebook
        facebook.setOnClickListener {
            Utils.singleClick()
            val url = "https://www.facebook.com/asianliftbangladesh"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        val linkedin = binding.linkedin
        linkedin.setOnClickListener {
            Utils.singleClick()
            val url = "https://www.linkedin.com/asianliftbangladesh"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        val aboutApp = binding.aboutApp
        aboutApp.setOnClickListener {
            Utils.singleClick()
            startActivity(Intent(activity, AboutApp::class.java))
        }

        val textViewLogout = binding.logout
        textViewLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(activity, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}