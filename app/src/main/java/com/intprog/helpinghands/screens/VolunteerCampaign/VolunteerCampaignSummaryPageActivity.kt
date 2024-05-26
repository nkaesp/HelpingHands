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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R
import com.intprog.helpinghands.models.CampaignType
import java.util.*

class VolunteerCampaignSummaryPageActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_summary)

        db = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        val title = intent.getStringExtra("title")
        val category = intent.getStringExtra("category")
        val description = intent.getStringExtra("description")
        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val age = intent.getStringExtra("age")
        val location = intent.getStringExtra("location")
        val imageUriString = intent.getStringExtra("imageUri")

        findViewById<TextView>(R.id.titleTextView).text = title
        findViewById<TextView>(R.id.categoryTextView).text = category
        findViewById<TextView>(R.id.descTextView).text = description
        findViewById<TextView>(R.id.startDateTextView).text = startDate
        findViewById<TextView>(R.id.endDateTextView).text = endDate
        findViewById<TextView>(R.id.ageTextView).text = age
        findViewById<TextView>(R.id.locationTextView).text = location

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
                val post = VolunteerCampaignPost(
                    title ?: "", category ?: "", description ?: "", startDate ?: "",
                    endDate ?: "", age ?: "", location ?: "", imageUriString, CampaignType.VOLUNTEER
                )
                saveVolunteerCampaignPost(post)
            } else {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveVolunteerCampaignPost(post: VolunteerCampaignPost) {
        db.collection("volunteer_campaign_posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Volunteer campaign post added successfully", Toast.LENGTH_SHORT).show()
                uploadImageToStorage(post.imageUri)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding volunteer campaign post: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToStorage(imageUri: String?) {
        if (imageUri.isNullOrEmpty()) return

        val imageRef = storageRef.child("volunteer_campaign_post_images/${UUID.randomUUID()}")
        imageRef.putFile(Uri.parse(imageUri))
            .addOnSuccessListener {
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, VolunteerCampaignSelectionPageActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
