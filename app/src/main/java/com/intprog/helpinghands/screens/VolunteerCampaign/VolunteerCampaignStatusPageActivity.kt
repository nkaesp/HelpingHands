package com.intprog.helpinghands.screens.VolunteerCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R

class VolunteerCampaignStatusPageActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_status)

        val titleTextView = findViewById<TextView>(R.id.title)
        val categoryTextView = findViewById<TextView>(R.id.categoryTextView)
        val descTextView = findViewById<TextView>(R.id.descTextView)
        val startDateTextView = findViewById<TextView>(R.id.startDateTextView)
        val endDateTextView = findViewById<TextView>(R.id.endDateTextView)
        val ageTextView = findViewById<TextView>(R.id.ageTextView)
        val locationTextView = findViewById<TextView>(R.id.locationTextView)
        val imageView = findViewById<ImageView>(R.id.postedpic)
        val deletePostButton = findViewById<Button>(R.id.deletePostButton)

        val title = intent.getStringExtra("title")
        val category = intent.getStringExtra("category")
        val description = intent.getStringExtra("description")
        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val age = intent.getStringExtra("age")
        val location = intent.getStringExtra("location")
        val imageUri = intent.getStringExtra("imageUri")

        titleTextView.text = title
        categoryTextView.text = category
        descTextView.text = description
        startDateTextView.text = startDate
        endDateTextView.text = endDate
        ageTextView.text = age
        locationTextView.text = location

        if (imageUri != null) {
            Glide.with(this)
                .load(Uri.parse(imageUri))
                .into(imageView)
        }

        deletePostButton.setOnClickListener {
            val documentId = intent.getStringExtra("documentId")

            if (documentId != null) {
                // Delete the post from Firestore
                db.collection("volunteer_campaign_posts").document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show()

                        // Navigate back to the home page
                        val intent = Intent(this, VolunteerCampaignSelectionPageActivity::class.java)
                        startActivity(intent)
                        finish()
                        overridePendingTransition(0, 0)
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this,
                            "Failed to delete post: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
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

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            onBackPressed()
            overridePendingTransition(0, 0)
        }
    }
}
