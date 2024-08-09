package com.asianliftbd.staff.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.asianliftbd.staff.R
import com.asianliftbd.staff.Utils
import com.asianliftbd.staff.databinding.ActivityOfferNewBinding
import com.asianliftbd.staff.model.OfferModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OfferNew : AppCompatActivity() {
    private lateinit var binding: ActivityOfferNewBinding

    private var project: String? = null
    private var address: String? = null
    private var liftType: String? = null
    private var workType: String? = null
    private var unit: String? = null
    private var floor: String? = null
    private var shaft: String? = null
    private var person: String? = null
    private var note: String? = null

    private val uid = FirebaseAuth.getInstance().uid
    private val userName = FirebaseAuth.getInstance().currentUser!!.displayName

    private val ref = FirebaseDatabase.getInstance().reference.child("-offer")
    private var refOffer: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOfferNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val extras = intent.extras
        if (extras != null) {
            val key = extras.getString("key")
            refOffer = ref.child("$key")
            refOffer?.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        supportActionBar?.title = "Update Offer"
                        val data = snapshot.getValue(OfferModel::class.java)
                        binding.projectName.setText(data?.name)
                        binding.address.setText(data?.address)
                        binding.productDropDown.setText(data?.ptype, false)
                        binding.workDropDown.setText(data?.wtype, false)
                        binding.unit.setText(data?.unit)
                        binding.floor.setText(data?.floor)
                        binding.shaft.setText(data?.shaft)
                        binding.person.setText(data?.person)
                        binding.note.setText(data?.note)
                        binding.submitButton.text = resources.getString(R.string.update)
                        binding.deleteButton.visibility = View.VISIBLE
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

        binding.projectName.doOnTextChanged { text, _, _, _ ->
            project = text.toString()
            if (project.isNullOrEmpty()) {
                binding.projectNameLayout.isErrorEnabled = true
                binding.projectNameLayout.error = "Required"
            } else {
                binding.projectNameLayout.isErrorEnabled = false
            }
        }

        ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.liftType)
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
            binding.productDropDown.setAdapter(adapter)
        }

        ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.workType)
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
            binding.workDropDown.setAdapter(adapter)
        }

        binding.address.doOnTextChanged { text, _, _, _ -> address = text.toString() }
        binding.productDropDown.doOnTextChanged { text, _, _, _ -> liftType = text.toString() }
        binding.workDropDown.doOnTextChanged { text, _, _, _ -> workType = text.toString() }
        binding.unit.doOnTextChanged { text, _, _, _ -> unit = text.toString() }
        binding.floor.doOnTextChanged { text, _, _, _ -> floor = text.toString() }
        binding.shaft.doOnTextChanged { text, _, _, _ -> shaft = text.toString() }
        binding.person.doOnTextChanged { text, _, _, _ -> person = text.toString() }
        binding.note.doOnTextChanged { text, _, _, _ -> note = text.toString() }

        binding.submitButton.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            if (binding.projectName.text.isNullOrEmpty()) {
                binding.projectNameLayout.isErrorEnabled = true
                binding.projectNameLayout.error = "Required"
                binding.projectNameLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.submitButton.visibility = View.GONE

                if (refOffer == null) {
                    refOffer = ref.push()
                    refOffer = ref.child("${Utils.setDateDatabaseFormat()}${refOffer!!.key}")
                }

                val data = OfferModel(
                    name = project!!,
                    address = address,
                    ptype = liftType,
                    wtype = workType!!,
                    unit = unit,
                    floor = floor,
                    shaft = shaft,
                    person = person,
                    note = note,
                    refer = userName,
                    date = Utils.setDateTextFormat(),
                    uid = uid!!
                )

                refOffer!!.setValue(data) {error, _ ->
                    if (error != null) {
                        binding.progressBar.visibility = View.GONE
                        binding.submitButton.visibility = View.VISIBLE
                        Snackbar.make(it, "Error Occurred! Try again", Snackbar.LENGTH_SHORT)
                            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(resources.getColor(R.color.md_theme_error, null))
                            .show()
                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.submitButton.visibility = View.VISIBLE
                        Snackbar.make(it, "Saved successfully", Snackbar.LENGTH_SHORT)
                            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                        finish()
                    }
                }
            }
        }

        binding.deleteButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = LayoutInflater.from(this).inflate(R.layout.modal_bottom_sheet, null)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()

            val title = view.findViewById<TextView>(R.id.title)
            val details = view.findViewById<TextView>(R.id.details)
            val actionButton = view.findViewById<Button>(R.id.actionButton)
            val cancelButton = view.findViewById<Button>(R.id.cancelButton)

            title.text = "Delete Offer"
            details.text = "Are you sure you want to delete this offer?"
            actionButton.text = "Delete"
            cancelButton.text = "Cancel"

            cancelButton.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            actionButton.setOnClickListener {
                refOffer?.removeValue { _: DatabaseError?, _: DatabaseReference? ->
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