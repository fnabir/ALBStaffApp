package com.asianliftbd.staff.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.asianliftbd.staff.R
import com.asianliftbd.staff.databinding.ActivityPasswordChangeBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class PasswordChange : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordChangeBinding

    private var auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val userMail = user!!.email

    private var oldPassword : String? = null
    private var newPassword : String? = null
    private var confirmNewPassword : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPasswordChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.email.setText(userMail)

        binding.oldPassword.doOnTextChanged { text, _, _, _ ->
            oldPassword = text.toString()
            if (oldPassword.isNullOrEmpty()){
                binding.oldPasswordLayout.isErrorEnabled = true
                binding.oldPasswordLayout.error = "Type your current password!"
            } else {
                binding.oldPasswordLayout.isErrorEnabled = false
            }
        }

        binding.newPassword.doOnTextChanged { text, _, _, _ ->
            newPassword = text.toString()
            if (newPassword.isNullOrEmpty()){
                binding.newPasswordLayout.isErrorEnabled = true
                binding.newPasswordLayout.error = "Type your new password!"
            } else if (newPassword!!.length < 6) {
                binding.newPasswordLayout.isErrorEnabled = true
                binding.newPasswordLayout.error = "New password must be at least 6 characters long!"
            } else if (confirmNewPassword != newPassword) {
                binding.confirmNewPasswordLayout.isErrorEnabled = true
                binding.confirmNewPasswordLayout.error = "New Password does not match with Confirm Password!"
            } else {
                binding.newPasswordLayout.isErrorEnabled = false
                binding.confirmNewPasswordLayout.isErrorEnabled = false
            }
        }

        binding.confirmNewPassword.doOnTextChanged { text, _, _, _ ->
            confirmNewPassword = text.toString()

            if (confirmNewPassword.isNullOrEmpty()){
                binding.confirmNewPasswordLayout.isErrorEnabled = true
                binding.confirmNewPasswordLayout.error = "Re-type your new password!"
            } else if (confirmNewPassword != newPassword) {
                binding.confirmNewPasswordLayout.isErrorEnabled = true
                binding.confirmNewPasswordLayout.error = "New Password does not match with Confirm Password!"
            } else {
                binding.confirmNewPasswordLayout.isErrorEnabled = false
            }
        }

        binding.submitButton.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            if (oldPassword.isNullOrEmpty()) {
                binding.oldPasswordLayout.isErrorEnabled = true
                binding.oldPasswordLayout.error = "Type your current password!"
                binding.oldPasswordLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            } else if (newPassword.isNullOrEmpty()) {
                binding.newPasswordLayout.isErrorEnabled = true
                binding.newPasswordLayout.error = "Type your new password!"
                binding.newPasswordLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            } else if (confirmNewPassword.isNullOrEmpty()) {
                binding.confirmNewPasswordLayout.isErrorEnabled = true
                binding.confirmNewPasswordLayout.error = "Confirm your new password!"
                binding.confirmNewPasswordLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            } else if (newPassword != confirmNewPassword) {
                binding.confirmNewPasswordLayout.isErrorEnabled = true
                binding.confirmNewPasswordLayout.error = "New Password does not match with Confirm Password!"
                binding.confirmNewPasswordLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            } else {
                binding.submitButton.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE

                val credential = EmailAuthProvider.getCredential(userMail!!, oldPassword!!)

                user!!.reauthenticate(credential).addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPassword!!).addOnCompleteListener {
                            Toast.makeText(this,"Password Changed Successfully.", Toast.LENGTH_SHORT).show()
                            onBackPressedDispatcher.onBackPressed()
                        }
                    } else {
                        Toast.makeText(this,"Wrong password!", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                        binding.submitButton.visibility = View.VISIBLE
                        binding.oldPassword.setText("")
                        binding.oldPasswordLayout.isErrorEnabled = true
                        binding.oldPasswordLayout.error = "Password was incorrect!"
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}