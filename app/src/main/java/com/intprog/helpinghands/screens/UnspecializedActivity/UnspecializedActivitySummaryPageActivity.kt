package com.intprog.helpinghands.screens.UnspecializedActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R
import com.intprog.helpinghands.model.UnspecializedActivityPost
import com.intprog.helpinghands.models.CampaignType
import java.util.*

class UnspecializedActivitySummaryPageActivity : AppCompatActivity() {

    private lateinit var storageRef: StorageReference
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unspecialized_summary)

        storageRef = FirebaseStorage.getInstance().reference

        val backButton: ImageButton = findViewById(R.id.backTop)
        backButton.setOnClickListener {
            onBackPressed()
            overridePendingTransition(0, 0)
        }

        val summaryImageButton: ImageButton = findViewById(R.id.summaryImageButton)
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
            if (!title.isNullOrEmpty() && !noOfParticipants.isNullOrEmpty() && !description.isNullOrEmpty() && !imageUriString.isNullOrEmpty()) {
                val post = UnspecializedActivityPost(title ?: "", noOfParticipants ?: "", description ?: "", imageUriString, CampaignType.UNSPECIALIZED)
                saveUnspecializedPostToFirestore(post)
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

    private fun saveUnspecializedPostToFirestore(post: UnspecializedActivityPost) {
        db.collection("unspecialized_activity_posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Unspecialized activity post saved successfully", Toast.LENGTH_SHORT).show()
                uploadImageToStorage(post.imageUri)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save unspecialized activity post: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToStorage(imageUri: String?) {
        if (imageUri.isNullOrEmpty()) return

        val imageRef = storageRef.child("unspecialized_activity_post_images/${UUID.randomUUID()}")
        imageRef.putFile(Uri.parse(imageUri))
            .addOnSuccessListener {
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, UnspecializedActivitySelectionPageActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
