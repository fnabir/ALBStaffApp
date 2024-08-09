package com.asianliftbd.staff.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.asianliftbd.staff.R
import com.asianliftbd.staff.databinding.ActivityEditProfileBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditProfile : AppCompatActivity() {

    private val tag = "EditProfile"
    private lateinit var binding: ActivityEditProfileBinding

    private var name : String? = null
    private var phone : String? = null

    private var auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private var ref = FirebaseDatabase.getInstance().reference.child("info/user/" + auth.uid)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val nameText : TextInputEditText = binding.name
        val nameTextLayout : TextInputLayout = binding.nameTextLayout

        val phoneText : TextInputEditText = binding.phone
        val phoneTextLayout : TextInputLayout = binding.phoneTextLayout

        binding.email.setText(auth.currentUser!!.email)

        nameText.doOnTextChanged{ text, _, _, _ ->
            name = nameText.text.toString()
            if (text.isNullOrEmpty()) {
                nameTextLayout.isErrorEnabled = true
                nameTextLayout.error = "Name Required"
            } else if (text.length < 3) {
                nameTextLayout.isErrorEnabled = true
                nameTextLayout.error = "Full Name Required"
            } else {
                nameTextLayout.isErrorEnabled = false
            }
        }

        phoneText.doOnTextChanged { text, _, _, _ ->
            phone = phoneText.text.toString()
            if (text.isNullOrEmpty()) {
                phoneTextLayout.isErrorEnabled = true
                phoneTextLayout.error = "Phone Number Required"
            } else if (text.length != 11) {
                phoneTextLayout.isErrorEnabled = true
                phoneTextLayout.error = "Type your 11-digit number"
            } else {
                phoneTextLayout.isErrorEnabled = false
            }
        }

        val previousName = user!!.displayName
        var previousPhone = ""

        ref.child("phone").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                previousPhone = if (d.exists()) d.value.toString()
                else ""
                phoneText.setText(previousPhone)
            }

            override fun onCancelled(e: DatabaseError) {
                Log.e(tag,e.toString())
            }
        })

        if (previousName!!.isNotEmpty()) nameText.setText(previousName)
        else nameText.setText("")

        val saveButton = binding.saveButton
        val progressBar = binding.progressBar

        saveButton.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            if (!name.isNullOrEmpty() && name == previousName && !phone.isNullOrEmpty() && phone == previousPhone) {
                progressBar.visibility = View.GONE
                onBackPressedDispatcher.onBackPressed()
            } else {
                saveButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                if (nameTextLayout.isErrorEnabled || phoneTextLayout.isErrorEnabled) {
                    saveButton.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    Snackbar.make(it, "No Internet Connection", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(resources.getColor(R.color.md_theme_error, null))
                        .show()
                } else {
                    val updateUserName = UserProfileChangeRequest.Builder()
                        .setDisplayName(name).build()
                    user.updateProfile(updateUserName)
                    ref.child("name").setValue(name)
                    ref.child("phone").setValue(phone)
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@EditProfile, "Saved successfully", Toast.LENGTH_SHORT).show()
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}