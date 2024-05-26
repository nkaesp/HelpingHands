package com.intprog.helpinghands.screens.VolunteerCampaign

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import com.intprog.helpinghands.CampaignPostingOptionsPageActivity
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R
import java.util.Calendar

class VolunteerCampaignPostingPageActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_GALLERY = 1001
    private lateinit var uploadedImageView: ImageButton
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_posting_page)

        val ageSpinner = findViewById<Spinner>(R.id.ageSpinner)
        val ageChoices = arrayOf("18+", "17+", "16+", "15+")
        val adapter = AgeSpinnerAdapter(this, ageChoices)
        ageSpinner.adapter = adapter

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

        val startDateEditText = findViewById<Button>(R.id.startDateEditText)
        val endDateEditText = findViewById<Button>(R.id.endDateEditText)

        startDateEditText.setOnClickListener {
            showDatePickerDialog(startDateEditText)
        }

        endDateEditText.setOnClickListener {
            showDatePickerDialog(endDateEditText)
        }

        val button = findViewById<Button>(R.id.continueButton)
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val categoryEditText = findViewById<EditText>(R.id.categoryEditText)
        val descEditText = findViewById<EditText>(R.id.descEditText)
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
            val endDate = endDateEditText.text.toString()
            val age = ageSpinner.selectedItem.toString()
            val location = locationEditText.text.toString()

            if (title.isEmpty() || category.isEmpty() || description.isEmpty() ||
                startDate.isEmpty() || endDate.isEmpty() || age.isEmpty() || location.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please fill in all fields.")
                    .setPositiveButton("OK", null)
                    .show()
            } else if (selectedImageUri == null) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please select an image.")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                val intent = Intent(this, VolunteerCampaignSummaryPageActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("category", category)
                intent.putExtra("description", description)
                intent.putExtra("startDate", startDate)
                intent.putExtra("endDate", endDate)
                intent.putExtra("age", age)
                intent.putExtra("location", location)
                intent.putExtra("imageUri", selectedImageUri.toString())
                startActivity(intent)
                overridePendingTransition(0, 0)

                titleEditText.text.clear()
                categoryEditText.text.clear()
                descEditText.text.clear()
                startDateEditText.text = ""
                endDateEditText.text = ""
                ageSpinner.setSelection(0)
                locationEditText.text.clear()

            }
        }


        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            showConfirmationDialog()
        }

        val homeImageButton = findViewById<ImageButton>(R.id.homeImageButton)
        homeImageButton.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        val profileImageButton = findViewById<ImageButton>(R.id.profileImageButton)
        profileImageButton.setOnClickListener {
            val intent = Intent(this, ProfilePageActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    private fun showDatePickerDialog(button: Button) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Update the button text with the selected date
                button.text = "$dayOfMonth/${monthOfYear + 1}/$year"
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
        overridePendingTransition(0, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            uploadedImageView.setImageURI(selectedImageUri)
        }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure you want discard your post?")
            .setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
                overridePendingTransition(0, 0)
            }
            .setNegativeButton("No", null)
            .show()
    }
}