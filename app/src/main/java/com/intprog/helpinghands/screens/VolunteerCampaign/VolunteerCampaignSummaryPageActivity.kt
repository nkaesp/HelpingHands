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
import com.intprog.helpinghands.models.CampaignType
import com.intprog.helpinghands.screens.UnspecializedActivity.UnspecializedActivitySelectionPageActivity

class VolunteerCampaignSummaryPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_summary)

        val title = intent.getStringExtra("title")
        val category = intent.getStringExtra("category")
        val description = intent.getStringExtra("description")
        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val age = intent.getStringExtra("age")
        val location = intent.getStringExtra("location")

        findViewById<TextView>(R.id.titleTextView).text = title
        findViewById<TextView>(R.id.categoryTextView).text = category
        findViewById<TextView>(R.id.descTextView).text = description
        findViewById<TextView>(R.id.startDateTextView).text = startDate
        findViewById<TextView>(R.id.endDateTextView).text = endDate
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
            overridePendingTransition(0, 0)
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

        val postButton = findViewById<Button>(R.id.postButton)
        postButton.setOnClickListener {
            if (!title.isNullOrEmpty() && !category.isNullOrEmpty() && !description.isNullOrEmpty()
                && !startDate.isNullOrEmpty() && !endDate.isNullOrEmpty() && !age.isNullOrEmpty()
                && !location.isNullOrEmpty() && !imageUriString.isNullOrEmpty()) {
                val post = VolunteerCampaignPost(title ?: "",  category ?: "",  description ?: "", startDate ?: "" ,  endDate ?: "",  age ?: "", location ?: "", imageUriString, CampaignType.VOLUNTEER)
                val intent = Intent(this, VolunteerCampaignSelectionPageActivity::class.java).apply {
                    putExtra("post", post)

                    putExtra("title", title)
                    putExtra("category", category)
                    putExtra("description", description)
                    putExtra("startDate", startDate)
                    putExtra("endDate", endDate)
                    putExtra("age", age)
                    putExtra("location", location)
                    putExtra("imageUri", imageUriString)
                    putExtra("type", post.type.name)
                }
                startActivity(intent)
                overridePendingTransition(0, 0)
            } else {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}