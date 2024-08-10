package com.asianliftbd.staff.activity

import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.asianliftbd.staff.BuildConfig
import com.asianliftbd.staff.R
import com.asianliftbd.staff.Utils
import com.asianliftbd.staff.databinding.ActivityAboutAppBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AboutApp : AppCompatActivity() {
    private lateinit var binding: ActivityAboutAppBinding

    private val version: String = BuildConfig.VERSION_NAME
    private val versionCode = BuildConfig.VERSION_CODE

    private var newVersion: String? = null
    private var newVersionCode = 0

    private var url: String? = null
    val ref = FirebaseDatabase.getInstance().reference.child("app")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val changeLog : Array<ArrayList<Any>> = arrayOf (
            arrayListOf("3.0.0", 10, "11 Aug 2024",
                "- Crash on opening Staff Balance page.\n" +
                        "- Crash on saving new offer when work typ is not set.\n" +
                        "- Saving a transaction for staff would change the order of the staff balance page.",
                "- Forget Password page gets the mail from Login page if the typed mail is valid.\n" +
                        "- Restrict date selection from the future.\n" +
                        "- Optimized database functions.\n" +
                        "- Minimum system requirement changed from Android 10 to Android 12.\n" +
                        "- Full Changelog is available in About App Page.",
                "- Introduced Material 3 Design.\n" +
                        "- Light and Dark Mode which changes with System Settings.\n" +
                        "- Added option in keyboard to take automatically to next input field.\n"),
            arrayListOf("2.3.0", 9, "14 Jun 2024",
                "- Adding new callback record would make a wrong entry and caused crash.",
                "- UI updated to modern UI.\n" +
                        "- Major Bug fixes and database optimizations.\n" +
                        "- Updated font with mono spaced font.\n" +
                        "- Removed contact info page.\n" +
                        "- Minimum system requirement changed from Android 6 to Android 10.",
                "- Added functionality to let user edit or delete callback if user entered the record.")
        )

        ref.child("staff").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                if (d.child("code").exists()) {
                    newVersionCode = d.child("code").value.toString().toInt()
                    if (newVersionCode > versionCode) {
                        newVersion = d.child("version").value.toString()
                        binding.updateResult.text = "New update available"
                        binding.updateVersion.text = "$newVersion ($newVersionCode)"
                        binding.updateReleaseDate.text = d.child("date").value.toString()
                        binding.updateLayout.visibility = View.VISIBLE
                    } else binding.updateLayout.visibility = View.GONE
                } else {
                    binding.updateLayout.visibility = View.GONE
                }
            }
            override fun onCancelled(e: DatabaseError) {}
        })

        val versionText = "$version ($versionCode)"
        binding.version.text = versionText

        for (i in changeLog.indices) {
            if (versionCode == changeLog[i][1] as Int) {
                binding.releaseDate.text = changeLog[i][2].toString()
                binding.whatsNewLayout.visibility = View.VISIBLE
                if (changeLog[i][3] == "") binding.fixedLayout.visibility = View.GONE
                else {
                    binding.fixedLayout.visibility = View.VISIBLE
                    binding.fixed.text = changeLog[i][3].toString()
                }

                if (changeLog[i][4] == "") binding.updatedLayout.visibility = View.GONE
                else {
                    binding.updatedLayout.visibility = View.VISIBLE
                    binding.updated.text = changeLog[i][4].toString()
                }

                if (changeLog[i][5] == "") binding.addedLayout.visibility = View.GONE
                else {
                    binding.addedLayout.visibility = View.VISIBLE
                    binding.added.text = changeLog[i][5].toString()
                }
            }

            else {
                binding.changelogLayout.visibility = View.VISIBLE
                val fixed = if (changeLog[i][3].toString().isEmpty()) "" else "## Fixed\n${changeLog[i][3]}\n"
                val updated = if (changeLog[i][4].toString().isEmpty()) "" else "## Updated\n${changeLog[i][4]}\n"
                val added = if (changeLog[i][5].toString().isEmpty()) "" else "## Added\n${changeLog[i][5]}\n"
                val text = "${changeLog[i][0]}(${changeLog[i][1]}) - ${changeLog[i][2]}\n\n$fixed$updated$added"
                val textView = TextView(this)
                textView.text = text
                textView.setPadding(10)
                textView.setTextAppearance(R.style.text_small)
                binding.changelogLayout.addView(textView)
            }
        }

        binding.downloadButton.setOnClickListener {
            val fileName = "ALB Staff-$newVersion($newVersionCode).apk"
            if (Utils.isInternetAvailable(this)) {
                downloadFile(fileName, url)
            } else {
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun downloadFile(fileName: String, url: String?) {
        binding.downloadButton.visibility = View.GONE
        val uri = Uri.parse("https://asianliftbd.com/apk/ALBStaff-$newVersion($newVersionCode).apk")
        val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        try {
            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle(fileName)
            request.setDescription("App Update")
            request.setNotificationVisibility(1)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            manager.enqueue(request)
            Toast.makeText(this, "Download started!", Toast.LENGTH_SHORT).show()
            binding.downloadButton.visibility = View.VISIBLE
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show()
            Log.e("Update App Download Error", e.toString())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}