package com.intprog.helpinghands.screens.VolunteerCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R
import com.intprog.helpinghands.screens.UnspecializedActivity.UnspecializedActivitySelectionPageActivity

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
        findViewById<TextView>(R.id.endDateTextView).text = duration
        findViewById<TextView>(R.id.ageTextView).text = age
        findViewById<TextView>(R.id.locationTextView).text = location

        val imageUriString = intent.getStringExtra("imageUri")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            findViewById<ImageView>(R.id.uploadedImageView).setImageURI(imageUri)
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

        val postButton = findViewById<Button>(R.id.postButton)
        postButton.setOnClickListener {
            if (!title.isNullOrEmpty() && !category.isNullOrEmpty() && !description.isNullOrEmpty()
                && !startDate.isNullOrEmpty() && !duration.isNullOrEmpty() && !age.isNullOrEmpty()
                && !location.isNullOrEmpty() && !imageUriString.isNullOrEmpty()) {
                val post = VolunteerCampaignPost(title ?: "",  category ?: "",  description ?: "", startDate ?: "" ,  duration ?: "",  age ?: "", imageUriString)
                val intent = Intent(this, VolunteerCampaignSelectionPageActivity::class.java).apply {
                    putExtra("post", post)

                    putExtra("title", title)
                    putExtra("category", category)
                    putExtra("description", description)
                    putExtra("startDate", startDate)
                    putExtra("duration", duration)
                    putExtra("age", age)
                    putExtra("location", location)
                    putExtra("imageUri", imageUriString)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}