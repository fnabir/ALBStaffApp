package com.asianliftbd.staff.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.asianliftbd.staff.R
import com.asianliftbd.staff.Utils
import com.asianliftbd.staff.databinding.ActivityCallbackNewBinding
import com.asianliftbd.staff.model.CallbackModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CallbackNew : AppCompatActivity() {
    private lateinit var binding: ActivityCallbackNewBinding

    private var name: String? = null
    private var project: String? = null
    private var details: String? = null
    private var date: String? = null

    private val user = FirebaseAuth.getInstance().currentUser

    private val ref = FirebaseDatabase.getInstance().reference
    private var refCallback: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCallbackNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val nameText = binding.name
        val nameLayout = binding.nameLayout
        val dateButton = binding.dateButton
        val submitButton = binding.submitButton
        val deleteButton = binding.deleteButton
        val progressBar = binding.progressBar

        val extras = intent.extras
        if (extras != null) {
            supportActionBar?.title = "Edit Callback"
            project = extras.getString("project").toString()
            val key = extras.getString("key").toString()
            refCallback = ref.child("callback/$project/$key")
            refCallback?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        name = snapshot.child("name").value.toString()
                        details = snapshot.child("details").value.toString()
                        date = snapshot.child("date").value.toString()
                        binding.name.setText(name)
                        binding.projectDropDown.setText(project)
                        binding.details.setText(details)
                        binding.dateButton.text = date
                    } else {
                        finish()
                        Toast.makeText(this@CallbackNew, "Error Occurred! Try again", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        } else {
            deleteButton.visibility = View.GONE
        }

        binding.name.setText(user!!.displayName)

        nameText.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isEmpty()) {
                nameLayout.isErrorEnabled = true
            } else {
                nameLayout.isErrorEnabled = false
                name = text.toString()
            }
        }

        ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.projectName)
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
            binding.projectDropDown.setAdapter(adapter)
        }

        binding.projectDropDown.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isEmpty()) {
                binding.projectLayout.isErrorEnabled = true
                binding.projectLayout.error = "Project Name Required"
            } else {
                binding.projectLayout.isErrorEnabled = false
                project = text.toString()
            }
        }

        binding.details.doOnTextChanged { text, _, _, _ ->
            details = text.toString()
        }

        binding.details.setOnEditorActionListener{ _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_NEXT -> {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    dateButton.performClick()
                    true
                }
                else -> false
            }
        }

        dateButton.setOnClickListener {
            val today = MaterialDatePicker.todayInUtcMilliseconds()
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

            var selectedDate: Long

            if (dateButton.text == "Choose Date") {
                selectedDate = today
            } else {
                selectedDate = SimpleDateFormat("dd.MM.yy", Locale.getDefault()).parse(dateButton.text.toString())!!.time
                calendar.timeInMillis = selectedDate
                calendar.add(Calendar.DATE, 1)
                selectedDate = calendar.timeInMillis
            }
            calendar.timeInMillis = today
            calendar[Calendar.MONTH] = Calendar.JANUARY
            calendar[Calendar.YEAR] = 2021
            val startDate = calendar.timeInMillis

            val constraintsBuilder = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .setStart(startDate)
                .setFirstDayOfWeek(Calendar.SATURDAY)
                .build()

            val datePicker: MaterialDatePicker<*> = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setTextInputFormat(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()))
                .setSelection(selectedDate)
                .setCalendarConstraints(constraintsBuilder)
                .build()

            datePicker.addOnPositiveButtonClickListener {
                val date = SimpleDateFormat("dd.MM.yy", Locale.getDefault()).format(Date(it as Long))
                dateButton.text = date
            }
            datePicker.show(supportFragmentManager, "datePicker")
        }

        submitButton.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            if (name.isNullOrEmpty()) {
                binding.nameLayout.isErrorEnabled = true
                binding.nameLayout.error = "Name Required"
                binding.nameLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            } else if (project.isNullOrEmpty()) {
                binding.projectLayout.isErrorEnabled = true
                binding.projectLayout.error = "Project Name Required"
                binding.projectLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            } else if (details.isNullOrEmpty()) {
                binding.detailsLayout.isErrorEnabled = true
                binding.detailsLayout.error = "Details Required"
                binding.detailsLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            } else if (dateButton.text == "Choose Date") {
                Snackbar.make(it, "Please select date", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(resources.getColor(R.color.md_theme_error, null))
                    .show()
                binding.dateButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            } else {
                submitButton.visibility = View.GONE
                deleteButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

                val databaseDate = Utils.getDateDatabaseFormat(dateButton.text.toString())
                if (refCallback == null) {
                    refCallback = ref.child("callback/$project").push()
                    refCallback = ref.child("callback/$project/$databaseDate${refCallback!!.key}")
                }

                val data = CallbackModel(
                    name = name!!,
                    details = details!!,
                    date = dateButton.text.toString(),
                    uid = user.uid
                )

                refCallback?.setValue(data) { error, _ ->
                    if (error != null) {
                        Snackbar.make(it, "Error Occurred! Try again", Snackbar.LENGTH_SHORT).show()
                        submitButton.visibility = View.VISIBLE
                        deleteButton.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    } else {
                        Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        deleteButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = LayoutInflater.from(this).inflate(R.layout.modal_bottom_sheet, null)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()

            val title = view.findViewById<TextView>(R.id.title)
            val details = view.findViewById<TextView>(R.id.details)
            val actionButton = view.findViewById<Button>(R.id.actionButton)
            val cancelButton = view.findViewById<Button>(R.id.cancelButton)

            title.text = "Delete Transaction"
            details.text = "Are you sure you want to delete this transaction?"
            actionButton.text = "Delete"
            cancelButton.text = "Cancel"

            cancelButton.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            actionButton.setOnClickListener {
                refCallback?.removeValue { _: DatabaseError?, _: DatabaseReference? ->
                    bottomSheetDialog.dismiss()
                    Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}