package com.intprog.helpinghands.screens.VolunteerCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.R
import com.intprog.helpinghands.screens.DonationCampaign.DonationCampaignPostingPageActivity

class VolunteerCampaignSummaryPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_summary)

        val title = intent.getStringExtra("title")
        val category = intent.getStringExtra("category")
        val description = intent.getStringExtra("description")
        val startDate = intent.getStringExtra("startDate")
        val duration = intent.getStringExtra("duration")
        val age = intent.getStringExtra("age")
        val location = intent.getStringExtra("location")

        findViewById<TextView>(R.id.titleTextView).text = title
        findViewById<TextView>(R.id.categoryTextView).text = category
        findViewById<TextView>(R.id.descTextView).text = description
        findViewById<TextView>(R.id.startDateTextView).text = startDate
        findViewById<TextView>(R.id.durationTextView).text = duration
        findViewById<TextView>(R.id.ageTextView).text = age
        findViewById<TextView>(R.id.locationTextView).text = location

        val imageUriString = intent.getStringExtra("imageUri")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            findViewById<ImageView>(R.id.uploadedImageView).setImageURI(imageUri)
        }

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            val intent = Intent(this, VolunteerCampaignPostingPageActivity::class.java)
            startActivity(intent)
            finish()
        }

        val homeImageButton = findViewById<ImageButton>(R.id.homeImageButton)
        homeImageButton.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }


        val postButton = findViewById<Button>(R.id.postButton)
        postButton.setOnClickListener {
            // Create an intent to start VolunteerCampaignStatusPageActivity
            val intent = Intent(this, VolunteerCampaignStatusPageActivity::class.java).apply {
                // Pass the relevant data as extras
                putExtra("title", title)
                // Pass imageUriString if available
                if (imageUriString != null) {
                    putExtra("imageUri", imageUriString)
                }
            }
            // Start the activity
            startActivity(intent)
        }

    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure you want discard your post?")
            .setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton("No", null)
            .show()
    }
}
