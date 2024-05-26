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
import com.intprog.helpinghands.model.UnspecializedActivityPost
import com.intprog.helpinghands.models.CampaignType
import com.intprog.helpinghands.screens.UnspecializedActivity.UnspecializedActivitySelectionPageActivity
import java.util.*

class VolunteerCampaignSummaryPageActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_summary)

        db = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        val backButton: ImageButton = findViewById(R.id.backTop)
        backButton.setOnClickListener {
            onBackPressed()
            overridePendingTransition(0, 0)
        }

        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val categoryTextView: TextView = findViewById(R.id.categoryTextView)
        val descTextView: TextView = findViewById(R.id.descTextView)
        val startDateTextView: TextView = findViewById(R.id.startDateTextView)
        val endDateTextView: TextView = findViewById(R.id.endDateTextView)
        val ageTextView: TextView = findViewById(R.id.ageTextView)
        val locationTextView: TextView = findViewById(R.id.locationTextView)
        val uploadedImageView: ImageView = findViewById(R.id.uploadedImageView)

        val imageUriString = intent.getStringExtra("imageUri")

        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            uploadedImageView.setImageURI(imageUri)
        }

        val title = intent.getStringExtra("title")
        val category = intent.getStringExtra("category")
        val description = intent.getStringExtra("description")
        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val age = intent.getStringExtra("age")
        val location = intent.getStringExtra("location")

        titleTextView.text = title
        categoryTextView.text = category
        descTextView.text = description
        startDateTextView.text = startDate
        endDateTextView.text = endDate
        ageTextView.text = age
        locationTextView.text = location

        val postButton = findViewById<Button>(R.id.postButton)
        postButton.setOnClickListener {
            val post = createVolunteerCampaignPost()

            if (post != null) {
                uploadImageToStorage(post)
            } else {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
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
    }

    private fun createVolunteerCampaignPost(): VolunteerCampaignPost? {

        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val categoryTextView: TextView = findViewById(R.id.categoryTextView)
        val descTextView: TextView = findViewById(R.id.descTextView)
        val startDateTextView: TextView = findViewById(R.id.startDateTextView)
        val endDateTextView: TextView = findViewById(R.id.endDateTextView)
        val ageTextView: TextView = findViewById(R.id.ageTextView)
        val locationTextView: TextView = findViewById(R.id.locationTextView)

        val title = titleTextView.text.toString()
        val category = categoryTextView.text.toString()
        val description = descTextView.text.toString()
        val startDate = startDateTextView.text.toString()
        val endDate = endDateTextView.text.toString()
        val age = ageTextView.text.toString()
        val location = locationTextView.text.toString()
        val imageUriString = intent.getStringExtra("imageUri")

        return if (!title.isNullOrEmpty() && !category.isNullOrEmpty() && !description.isNullOrEmpty()
            && !startDate.isNullOrEmpty() && !endDate.isNullOrEmpty() && !age.isNullOrEmpty()
            && !location.isNullOrEmpty() && !imageUriString.isNullOrEmpty()) {
            VolunteerCampaignPost( title, category, description, startDate, endDate, age, location, imageUriString, CampaignType.VOLUNTEER)
        } else {
            null
        }
    }

    private fun saveVolunteerCampaignPostToFirestore(post: VolunteerCampaignPost) {
        db.collection("volunteer_campaign_posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                val documentId = documentReference.id
                val postWithId = post.copy(documentId = documentId)

                db.collection("volunteer_campaign_posts")
                    .document(documentId)
                    .set(postWithId)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Volunteer campaign post saved successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, VolunteerCampaignSelectionPageActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error updating volunteer campaign with ID: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save volunteer campaign post: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToStorage(post: VolunteerCampaignPost) {
        val imageUriString = post.imageUri

        if (imageUriString.isNullOrEmpty()) return

        val imageRef = storageRef.child("unspecialized_activity_post_images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(Uri.parse(imageUriString))

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully, now get the download URL
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Save post with image URL to Firestore
                val postWithImageUrl = post.copy(imageUri = uri.toString())
                saveVolunteerCampaignPostToFirestore(postWithImageUrl)
            }.addOnFailureListener { e ->
                // Handle any errors retrieving the download URL
                Toast.makeText(this, "Failed to retrieve image URL: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            // Handle unsuccessful uploads
            Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
