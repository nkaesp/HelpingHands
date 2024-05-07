package com.intprog.helpinghands.screens.UnspecializedActivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.R
import com.intprog.helpinghands.screens.VolunteerCampaign.VolunteerCampaignSummaryPageActivity

class UnspecializedActivityPostingPageActivity : AppCompatActivity() {

    private lateinit var uploadImageButton: ImageButton
    private var selectedImageUri: Uri? = null
    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unspecialized_posting)

        uploadImageButton = findViewById(R.id.uploadImageButton)

        uploadImageButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                )
            } else {
                launchImageSelectionIntent()
            }
        }

        val continueButton = findViewById<Button>(R.id.continueButton)
        continueButton.setOnClickListener {
            val titleEditText = findViewById<EditText>(R.id.titleEditText)
            val title = titleEditText.text.toString()

            val noOfParticipantsEditText = findViewById<EditText>(R.id.noOfParticipantsEditText)
            val noOfParticipants = noOfParticipantsEditText.text.toString()

            val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
            val description = descriptionEditText.text.toString()



            if (title.isEmpty() || noOfParticipants.isEmpty() || description.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please fill in all fields.")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                val intent = Intent(this, UnspecializedActivitySummaryPageActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("noOfParticipants", noOfParticipants)
                intent.putExtra("description", description)
                if (selectedImageUri != null) {
                    intent.putExtra("imageUri", selectedImageUri.toString())
                }

                startActivity(intent)
            }
        }

        val homeImageButton = findViewById<ImageButton>(R.id.homeImageButton)
        homeImageButton.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun launchImageSelectionIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchImageSelectionIntent()
                } else {
                    Toast.makeText(this, "Permission denied to access external storage", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            uploadImageButton.setImageURI(selectedImageUri)
        }
    }
}
