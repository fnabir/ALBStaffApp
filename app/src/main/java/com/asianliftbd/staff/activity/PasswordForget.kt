package com.asianliftbd.staff.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.asianliftbd.staff.R
import com.asianliftbd.staff.Utils
import com.asianliftbd.staff.databinding.ActivityPasswordForgetBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class PasswordForget : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordForgetBinding
    private var mail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPasswordForgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val mailText = binding.mail
        val mailTextLayout = binding.mailTextLayout

        if (intent.hasExtra("mail")) {
            mailText.setText(intent.getStringExtra("mail"))
            mailText.requestFocus()
        }

        mailText.doOnTextChanged { text, _, _, _ ->
            mail = text.toString()
            if (Utils.isValidEmail(mail)) {
                mailTextLayout.isErrorEnabled = false
            } else {
                mailTextLayout.isErrorEnabled = true
                mailTextLayout.error = "Invalid Email Address"
            }
        }

        val instructionText = binding.instructionText

        val submitButton = binding.submitButton
        val progressBar = binding.progressBar
        submitButton.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            if (mail.isEmpty()) {
                mailTextLayout.isErrorEnabled = true
                mailTextLayout.error = "Email Required"
                mailTextLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            } else if (mailTextLayout.isErrorEnabled) {
                mailTextLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            } else if (!Utils.isInternetAvailable(this)) {
                Snackbar.make(it, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(resources.getColor(R.color.md_theme_error, null))
                    .show()
            } else {
                submitButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                FirebaseAuth.getInstance().sendPasswordResetEmail(mail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mailTextLayout.isErrorEnabled = false
                        instructionText.text = String.format(
                            "Link to reset password sent to\n$mail\nFollow the link to reset your password."
                        )
                        instructionText.setTextColor(Color.parseColor("#28a745"))
                    } else {
                        mailTextLayout.isErrorEnabled = true
                        mailTextLayout.error = "Email not found!"
                    }
                    progressBar.visibility = View.GONE
                    submitButton.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}