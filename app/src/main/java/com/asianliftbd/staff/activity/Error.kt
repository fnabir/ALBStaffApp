package com.asianliftbd.staff.activity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.asianliftbd.staff.R
import com.asianliftbd.staff.databinding.ActivityErrorBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Error : AppCompatActivity() {
    private lateinit var binding: ActivityErrorBinding

    private lateinit var errorCodeInputLayout: TextInputLayout
    private lateinit var errorCodeEditText: TextInputEditText
    private lateinit var checkButton: Button
    private lateinit var detailsLayout: LinearLayout

    private var errorCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        errorCodeInputLayout = binding.errorCodeLayout
        errorCodeEditText = binding.errorCode
        checkButton = binding.checkButton
        detailsLayout = binding.detailsLayout

        errorCodeEditText.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isEmpty()) checkButton.visibility = GONE
            else {
                checkButton.visibility = VISIBLE
                errorCode = text.toString().toInt()
            }
        }

        errorCodeEditText.setOnEditorActionListener{ _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    checkButton.performClick()
                    true
                }
                else -> false
            }
        }

        val code = binding.code
        val description = binding.description
        val cause = binding.cause
        val level = binding.level
        val solution = binding.solution

        checkButton.setOnClickListener {
            detailsLayout.visibility = VISIBLE
            errorCodeInputLayout.visibility = GONE
            checkButton.visibility = GONE

            when (errorCode) {
                1 -> {
                    code.setText(R.string.e1)
                    description.setText(R.string.e1d)
                    cause.setText(R.string.e1c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e1s)
                }
                2 -> {
                    code.setText(R.string.e2)
                    description.setText(R.string.e2d)
                    cause.setText(R.string.e2c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e234s)
                }
                3 -> {
                    code.setText(R.string.e3)
                    description.setText(R.string.e3d)
                    cause.setText(R.string.e3c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e234s)
                }
                4 -> {
                    code.setText(R.string.e4)
                    description.setText(R.string.e4d)
                    cause.setText(R.string.e4c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e234s)
                }
                5 -> {
                    code.setText(R.string.e5)
                    description.setText(R.string.e5d)
                    cause.setText(R.string.e5c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e567s)
                }
                6 -> {
                    code.setText(R.string.e6)
                    description.setText(R.string.e6d)
                    cause.setText(R.string.e6c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e567s)
                }
                7 -> {
                    code.setText(R.string.e7)
                    description.setText(R.string.e7d)
                    cause.setText(R.string.e7c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e567s)
                }
                8 -> {
                    code.setText(R.string.e8)
                    description.setText(R.string.e8d)
                    cause.setText(R.string.e8c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e8s)
                }
                9 -> {
                    code.setText(R.string.e9)
                    description.setText(R.string.e9d)
                    cause.setText(R.string.e9c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e9s)
                }
                10 -> {
                    code.setText(R.string.e10)
                    description.setText(R.string.e10d)
                    cause.setText(R.string.e10c)
                    level.setText(R.string.el41)
                    solution.setText(R.string.e10s)
                }
                11 -> {
                    code.setText(R.string.e11)
                    description.setText(R.string.e11d)
                    cause.setText(R.string.e11c)
                    level.setText(R.string.el31)
                    solution.setText(R.string.e11s)
                }
                12 -> {
                    code.setText(R.string.e12)
                    description.setText(R.string.e12d)
                    cause.setText(R.string.e12c)
                    level.setText(R.string.el41)
                    solution.setText(R.string.e12s)
                }
                13 -> {
                    code.setText(R.string.e13)
                    description.setText(R.string.e13d)
                    cause.setText(R.string.e13c)
                    level.setText(R.string.el41)
                    solution.setText(R.string.e13s)
                }
                14 -> {
                    code.setText(R.string.e14)
                    description.setText(R.string.e14d)
                    cause.setText(R.string.e14c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e14s)
                }
                15 -> {
                    code.setText(R.string.e15)
                    description.setText(R.string.e15d)
                    cause.setText(R.string.e15c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e15s)
                }
                16 -> {
                    code.setText(R.string.e16)
                    description.setText(R.string.e16d)
                    cause.setText(R.string.e16c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e16s)
                }
                17 -> {
                    code.setText(R.string.e17)
                    description.setText(R.string.e17d)
                    cause.setText(R.string.e17c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e17s)
                }
                18 -> {
                    code.setText(R.string.e18)
                    description.setText(R.string.e18d)
                    cause.setText(R.string.e18c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e18s)
                }
                19 -> {
                    code.setText(R.string.e19)
                    description.setText(R.string.e19d)
                    cause.setText(R.string.e19c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e19s)
                }
                20 -> {
                    code.setText(R.string.e20)
                    description.setText(R.string.e20d)
                    cause.setText(R.string.e20c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e20s)
                }
                22 -> {
                    code.setText(R.string.e22)
                    description.setText(R.string.e22d)
                    cause.setText(R.string.e22c)
                    level.setText(R.string.el1)
                    solution.setText(R.string.e22s)
                }
                23 -> {
                    code.setText(R.string.e23)
                    description.setText(R.string.e23d)
                    cause.setText(R.string.e23c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e23s)
                }
                24 -> {
                    code.setText(R.string.e24)
                    description.setText(R.string.e24d)
                    cause.setText(R.string.e24c)
                    level.setText(R.string.el32)
                    solution.setText(R.string.e24s)
                }
                25 -> {
                    code.setText(R.string.e25)
                    description.setText(R.string.e25d)
                    cause.setText(R.string.e25c)
                    level.setText(R.string.el41)
                    solution.setText(R.string.e25s)
                }
                26 -> {
                    code.setText(R.string.e26)
                    description.setText(R.string.e26d)
                    cause.setText(R.string.e26c)
                    level.setText(R.string.el32)
                    solution.setText(R.string.e26s)
                }
                29 -> {
                    code.setText(R.string.e29)
                    description.setText(R.string.e29d)
                    cause.setText(R.string.e29c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e29s)
                }
                30 -> {
                    code.setText(R.string.e30)
                    description.setText(R.string.e30d)
                    cause.setText(R.string.e30c)
                    level.setText(R.string.el41)
                    solution.setText(R.string.e30s)
                }
                33 -> {
                    code.setText(R.string.e33)
                    description.setText(R.string.e33d)
                    cause.setText(R.string.e33c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e33s)
                }
                34 -> {
                    code.setText(R.string.e34)
                    description.setText(R.string.e34d)
                    cause.setText(R.string.e34c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e34s)
                }
                35 -> {
                    code.setText(R.string.e35)
                    description.setText(R.string.e35d)
                    cause.setText(R.string.e35c)
                    level.setText(R.string.el43)
                    solution.setText(R.string.e35s)
                }
                36 -> {
                    code.setText(R.string.e36)
                    description.setText(R.string.e36d)
                    cause.setText(R.string.e36c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e36s)
                }
                37 -> {
                    code.setText(R.string.e37)
                    description.setText(R.string.e37d)
                    cause.setText(R.string.e37c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e37s)
                }
                38 -> {
                    code.setText(R.string.e38)
                    description.setText(R.string.e38d)
                    cause.setText(R.string.e38c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e38s)
                }
                39 -> {
                    code.setText(R.string.e39)
                    description.setText(R.string.e39d)
                    cause.setText(R.string.e39c)
                    level.setText(R.string.el31)
                    solution.setText(R.string.e39s)
                }
                41 -> {
                    code.setText(R.string.e41)
                    description.setText(R.string.e41d)
                    cause.setText(R.string.e41c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e41s)
                }
                42 -> {
                    code.setText(R.string.e42)
                    description.setText(R.string.e42d)
                    cause.setText(R.string.e42c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e42s)
                }
                43 -> {
                    code.setText(R.string.e43)
                    description.setText(R.string.e43d)
                    cause.setText(R.string.e43c)
                    level.setText(R.string.el43)
                    solution.setText(R.string.e43s)
                }
                44 -> {
                    code.setText(R.string.e44)
                    description.setText(R.string.e44d)
                    cause.setText(R.string.e44c)
                    level.setText(R.string.el43)
                    solution.setText(R.string.e44s)
                }
                45 -> {
                    code.setText(R.string.e45)
                    description.setText(R.string.e45d)
                    cause.setText(R.string.e45c)
                    level.setText(R.string.el42)
                    solution.setText(R.string.e45s)
                }
                46 -> {
                    code.setText(R.string.e46)
                    description.setText(R.string.e46d)
                    cause.setText(R.string.e46c)
                    level.setText(R.string.el22)
                    solution.setText(R.string.e46s)
                }
                47 -> {
                    code.setText(R.string.e47)
                    description.setText(R.string.e47d)
                    cause.setText(R.string.e47c)
                    level.setText(R.string.el22)
                    solution.setText(R.string.e47s)
                }
                48 -> {
                    code.setText(R.string.e48)
                    description.setText(R.string.e48d)
                    cause.setText(R.string.e48c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e48s)
                }
                49 -> {
                    code.setText(R.string.e49)
                    description.setText(R.string.e49d)
                    cause.setText(R.string.e49c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e49s)
                }
                50 -> {
                    code.setText(R.string.e50)
                    description.setText(R.string.e50d)
                    cause.setText(R.string.e50c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e50s)
                }
                51 -> {
                    code.setText(R.string.e51)
                    description.setText(R.string.e51d)
                    cause.setText(R.string.e51c)
                    level.setText(R.string.el1)
                    solution.setText(R.string.e51s)
                }
                52 -> {
                    code.setText(R.string.e52)
                    description.setText(R.string.e52d)
                    cause.setText(R.string.e52c)
                    level.setText(R.string.el1)
                    solution.setText(R.string.e52s)
                }
                53 -> {
                    code.setText(R.string.e53)
                    description.setText(R.string.e53d)
                    cause.setText(R.string.e53c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e53s)
                }
                54 -> {
                    code.setText(R.string.e54)
                    description.setText(R.string.e54d)
                    cause.setText(R.string.e54c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e54s)
                }
                55 -> {
                    code.setText(R.string.e55)
                    description.setText(R.string.e55d)
                    cause.setText(R.string.e55c)
                    level.setText(R.string.el1)
                    solution.setText(R.string.e55s)
                }
                57 -> {
                    code.setText(R.string.e57)
                    description.setText(R.string.e57d)
                    cause.setText(R.string.e57c)
                    level.setText(R.string.el51)
                    solution.setText(R.string.e57s)
                }
                58 -> {
                    code.setText(R.string.e58)
                    description.setText(R.string.e58d)
                    cause.setText(R.string.e58c)
                    level.setText(R.string.el42)
                    solution.setText(R.string.e58s)
                }
                62 -> {
                    code.setText(R.string.e62)
                    description.setText(R.string.e62d)
                    cause.setText(R.string.e62c)
                    level.setText(R.string.el1)
                    solution.setText(R.string.e62s)
                }
                else -> {
                    code.text = "Code not found!"
                    Snackbar.make(it, "Code not found!", Snackbar.LENGTH_SHORT).show()
                    detailsLayout.visibility = GONE
                    errorCodeInputLayout.visibility = VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_search) {
            detailsLayout.visibility = GONE
            errorCodeInputLayout.visibility = VISIBLE
            errorCodeEditText.clearComposingText()
            errorCodeEditText.requestFocus()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}