package com.asianliftbd.staff.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.asianliftbd.staff.R
import com.asianliftbd.staff.Utils
import com.asianliftbd.staff.databinding.ActivityStartBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.initialize

class Start : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Firebase.initialize(context = this)

        binding.loginButton.setOnClickListener {
            Utils.singleClick()
            startActivity(Intent(this, Login::class.java))
        }

        binding.websiteButton.setOnClickListener {
            Utils.singleClick()
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://asianliftbd.com")
            startActivity(i)
        }

        binding.termsOfUse.setOnClickListener {
            Utils.singleClick()
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://asianliftbd.com/terms-of-use")
            startActivity(i)
        }
    }

    override fun onStart() {
        super.onStart()
        if (user != null) {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }
    }
}