package com.intprog.helpinghands.screens.UnspecializedActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R
import com.intprog.helpinghands.model.UnspecializedActivityPost
import com.intprog.helpinghands.models.CampaignType
import com.intprog.helpinghands.screens.DonationCampaign.DonationCampaignPost
import com.intprog.helpinghands.screens.DonationCampaign.DonationCampaignSelectionPageActivity
import java.util.*

class UnspecializedActivitySummaryPageActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unspecialized_summary)

        db = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        val backButton: ImageButton = findViewById(R.id.backTop)
        backButton.setOnClickListener {
            onBackPressed()
            overridePendingTransition(0, 0)
        }

        val summaryImageButton: ImageView = findViewById(R.id.summaryImageButton)
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val noOfParticipantsTextView: TextView = findViewById(R.id.noOfParticipantsTextView)
        val activityDescriptionTextView: TextView = findViewById(R.id.descriptionTextView)

        val imageUriString = intent.getStringExtra("imageUri")

        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            summaryImageButton.setImageURI(imageUri)
        }

        val title = intent.getStringExtra("title")
        val noOfParticipants = intent.getStringExtra("noOfParticipants")
        val description = intent.getStringExtra("description")

        titleTextView.text = title
        noOfParticipantsTextView.text = noOfParticipants
        activityDescriptionTextView.text = description

        val postButton = findViewById<Button>(R.id.postButton)
        postButton.setOnClickListener {
            val post = createUnspecializedActivityPost()

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

    private fun createUnspecializedActivityPost(): UnspecializedActivityPost? {
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val noOfParticipantsTextView: TextView = findViewById(R.id.noOfParticipantsTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val email = FirebaseAuth.getInstance().currentUser?.email
        val imageUriString = intent.getStringExtra("imageUri")
        val title = titleTextView.text.toString()
        val noOfParticipants = noOfParticipantsTextView.text.toString()
        val description = descriptionTextView.text.toString()

        return if (!title.isNullOrEmpty() && !noOfParticipants.isNullOrEmpty() && !description.isNullOrEmpty() && !imageUriString.isNullOrEmpty()) {
            UnspecializedActivityPost(title, noOfParticipants, description, email, imageUriString, CampaignType.UNSPECIALIZED)
        } else {
            null
        }
    }

    private fun saveUnspecializedPostToFirestore(post: UnspecializedActivityPost) {
        db.collection("unspecialized_activity_posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                val documentId = documentReference.id
                val postWithId = post.copy(documentId = documentId)

                db.collection("unspecialized_activity_posts")
                    .document(documentId)
                    .set(postWithId)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Unspecialized activity post saved successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, UnspecializedActivitySelectionPageActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error updating unspecialized activity with ID: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save unspecialized activity post: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToStorage(post: UnspecializedActivityPost) {
        val imageUriString = post.imageUri

        if (imageUriString.isNullOrEmpty()) return

        val imageRef = storageRef.child("unspecialized_activity_post_images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(Uri.parse(imageUriString))

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully, now get the download URL
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Save post with image URL to Firestore
                val postWithImageUrl = post.copy(imageUri = uri.toString())
                saveUnspecializedPostToFirestore(postWithImageUrl)
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

