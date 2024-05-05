package com.intprog.helpinghands.screens.DonationCampaign

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.intprog.helpinghands.R

class DonationCampaignPostingPageActivity : AppCompatActivity() {

    private lateinit var uploadImageButton: ImageButton
    private var selectedImageUri: Uri? = null
    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_posting_page)

        uploadImageButton = findViewById(R.id.uploadImageButton)

        uploadImageButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
            } else {
                launchImageSelectionIntent()
            }
        }

        val continueButton = findViewById<Button>(R.id.continueButton)
        continueButton.setOnClickListener {
            val titleEditText = findViewById<EditText>(R.id.titleEditText)
            val title = titleEditText.text.toString()

            val amountNeededEditText = findViewById<EditText>(R.id.amountNeededEditText)
            val amountNeeded = amountNeededEditText.text.toString()

            val tagEditText = findViewById<EditText>(R.id.tagEditText)
            val tag = tagEditText.text.toString()

            val fullNameEditText = findViewById<EditText>(R.id.fullNameEditText)
            val fullName = fullNameEditText.text.toString()

            val emailEditText = findViewById<EditText>(R.id.emailEditText)
            val email = emailEditText.text.toString()

            val phoneNumberEditText = findViewById<EditText>(R.id.phoneNumberEditText)
            val phoneNumber = phoneNumberEditText.text.toString()

            val contactMethodEditText = findViewById<EditText>(R.id.contactMethodEditText)
            val contactMethod = contactMethodEditText.text.toString()

            val intent = Intent(this, DonationCampaignSummaryPageActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("amountNeeded", amountNeeded)
            intent.putExtra("tag", tag)
            intent.putExtra("fullName", fullName)
            intent.putExtra("email", email)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("contactMethod", contactMethod)

            if (selectedImageUri != null) {
                intent.putExtra("imageUri", selectedImageUri.toString())
            }

            startActivity(intent)
        }
    }

    private fun launchImageSelectionIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }
}