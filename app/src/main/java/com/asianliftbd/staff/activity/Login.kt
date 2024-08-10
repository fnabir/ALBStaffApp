package com.asianliftbd.staff.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.asianliftbd.staff.BuildConfig
import com.asianliftbd.staff.R
import com.asianliftbd.staff.Utils
import com.asianliftbd.staff.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var mail: String = ""
    private var password: String = ""

    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val ref = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mailText = binding.mailText
        val mailTextLayout = binding.mailTextLayout
        val passwordText = binding.passwordText
        val passwordTextLayout = binding.passwordTextLayout
        val loginButton = binding.loginButton
        val progressBar = binding.progressBar

        mailText.doOnTextChanged { text, _, _, _ ->
            mail = text.toString()
            if (Utils.isValidEmail(mail)) {
                mailTextLayout.isErrorEnabled = false
            } else {
                mailTextLayout.isErrorEnabled = true
                mailTextLayout.error = "Invalid Email Address"
            }
        }

        passwordText.doOnTextChanged { text, _, _, _ ->
            password = text.toString()
            if (password.isEmpty()) {
                passwordTextLayout.isErrorEnabled = true
                passwordTextLayout.error = "Password Required"
            } else {
                passwordTextLayout.isErrorEnabled = false
            }
        }

        passwordText.setOnEditorActionListener{ _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    loginButton.performClick()
                    true
                }
                else -> false
            }
        }

        loginButton.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            if (mail.isEmpty() || mailTextLayout.isErrorEnabled) {
                mailTextLayout.isErrorEnabled = true
                mailText.requestFocus()
            } else if (password.isEmpty() || passwordTextLayout.isErrorEnabled) {
                passwordTextLayout.isErrorEnabled = true
                passwordText.requestFocus()
            } else if (!Utils.isInternetAvailable(this)) {
                Snackbar.make(it, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(resources.getColor(R.color.md_theme_error, null))
                    .show()
            } else {
                loginButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

                val model: String = Build.MODEL
                val android: String = Build.VERSION.RELEASE

                auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(this) { task: Task<AuthResult?> ->
                    progressBar.visibility = View.GONE
                    loginButton.visibility = View.VISIBLE
                    if (!task.isSuccessful) {
                        Snackbar.make(it, "Wrong Credentials", Snackbar.LENGTH_SHORT)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(resources.getColor(R.color.md_theme_error, null))
                            .show()
                    } else {
                        val refInfo = ref.child("info/user/${auth.uid}")
                        refInfo.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(d: DataSnapshot) {
                                if (d.child("role").value.toString().equals("staff", true)) {
                                    if (BuildConfig.BUILD_TYPE == "release") {
                                        refInfo.child("device").setValue("$model ($android)")
                                    }
                                    val i = Intent(this@Login, Dashboard::class.java)
                                    Snackbar.make(it, "Login Successful", Snackbar.LENGTH_SHORT)
                                        .show()
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(i)
                                } else {
                                    Snackbar.make(it, "Staff access denied", Snackbar.LENGTH_SHORT)
                                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                        .setBackgroundTint(resources.getColor(R.color.md_theme_error, null))
                                        .show()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                }
            }
        }

        binding.forgotButton.setOnClickListener {
            Utils.singleClick()
            val intent = Intent(this, PasswordForget::class.java)
            if (Utils.isValidEmail(mail))
                intent.putExtra("mail", mail)
            startActivity(intent)
        }

        binding.termsOfUseButton.setOnClickListener {
            Utils.singleClick()
            val url = "https://asianliftbd.com/terms-of-use"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
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