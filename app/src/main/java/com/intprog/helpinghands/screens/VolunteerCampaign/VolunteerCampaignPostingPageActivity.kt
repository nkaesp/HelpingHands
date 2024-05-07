package com.intprog.helpinghands.screens.VolunteerCampaign

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.intprog.helpinghands.CampaignPostingOptionsPageActivity
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.R

class VolunteerCampaignPostingPageActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_GALLERY = 1001
    private lateinit var uploadedImageView: ImageButton
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_posting_page)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_IMAGE_GALLERY
            )
        }

        val button = findViewById<Button>(R.id.continueButton)
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val categoryEditText = findViewById<EditText>(R.id.categoryEditText)
        val descEditText = findViewById<EditText>(R.id.descEditText)
        val startDateEditText = findViewById<EditText>(R.id.startDateEditText)
        val durationEditText = findViewById<EditText>(R.id.durationEditText)
        val ageEditText = findViewById<EditText>(R.id.ageEditText)
        val locationEditText = findViewById<EditText>(R.id.locationEditText)

        uploadedImageView = findViewById(R.id.uploadedImageView)
        uploadedImageView.setOnClickListener {
            openGallery()
            uploadedImageView.setImageResource(android.R.color.transparent) // Clear the image
            selectedImageUri = null
        }


        button.setOnClickListener {
            val title = titleEditText.text.toString()
            val category = categoryEditText.text.toString()
            val description = descEditText.text.toString()
            val startDate = startDateEditText.text.toString()
            val duration = durationEditText.text.toString()
            val age = ageEditText.text.toString()
            val location = locationEditText.text.toString()

            if (title.isEmpty() || category.isEmpty() || description.isEmpty() ||
                startDate.isEmpty() || duration.isEmpty() || age.isEmpty() || location.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please fill in all fields.")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                val intent = Intent(this, VolunteerCampaignSummaryPageActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("category", category)
                intent.putExtra("description", description)
                intent.putExtra("startDate", startDate)
                intent.putExtra("duration", duration)
                intent.putExtra("age", age)
                intent.putExtra("location", location)
                if (selectedImageUri != null) {
                    intent.putExtra("imageUri", selectedImageUri.toString())
                }
                startActivity(intent)

                titleEditText.text.clear()
                categoryEditText.text.clear()
                descEditText.text.clear()
                startDateEditText.text.clear()
                durationEditText.text.clear()
                ageEditText.text.clear()
                locationEditText.text.clear()

            }
        }

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            val intent = Intent(this, CampaignPostingOptionsPageActivity::class.java)
            startActivity(intent)
            finish()
        }

        val homeImageButton = findViewById<ImageButton>(R.id.homeImageButton)
        homeImageButton.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            uploadedImageView.setImageURI(selectedImageUri)
        }
    }
}