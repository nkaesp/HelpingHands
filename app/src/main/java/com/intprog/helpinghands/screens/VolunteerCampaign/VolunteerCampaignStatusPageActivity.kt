package com.intprog.helpinghands.screens.VolunteerCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R
import com.intprog.helpinghands.screens.DonationCampaign.DonationCampaignSelectionPageActivity

class VolunteerCampaignStatusPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_status)

        // Retrieve data from intent extras
        val title = intent.getStringExtra("title")
        val category = intent.getStringExtra("category")
        val description = intent.getStringExtra("description")
        val startDate = intent.getStringExtra("startDate")
        val duration = intent.getStringExtra("duration")
        val age = intent.getStringExtra("age")
        val location = intent.getStringExtra("location")

        // Populate views with the retrieved data
        findViewById<TextView>(R.id.title).text = title
        findViewById<TextView>(R.id.categoryTextView).text = category
        findViewById<TextView>(R.id.descTextView).text = description
        findViewById<TextView>(R.id.startDateTextView).text = startDate
        findViewById<TextView>(R.id.endDateTextView).text = duration
        findViewById<TextView>(R.id.ageTextView).text = age
        findViewById<TextView>(R.id.locationTextView).text = location

        // Retrieve and set image if available
        val imageUriString = intent.getStringExtra("imageUri")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            findViewById<ImageView>(R.id.postedpic).setImageURI(imageUri)
        }

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            onBackPressed()
        }

        val homeImageButton = findViewById<ImageButton>(R.id.homeImageButton)
        homeImageButton.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        val profileImageButton = findViewById<ImageButton>(R.id.profileImageButton)
        profileImageButton.setOnClickListener {
            val intent = Intent(this, ProfilePageActivity::class.java)
            startActivity(intent)
        }
    }
}