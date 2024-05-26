package com.intprog.helpinghands.screens.UnspecializedActivity

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

class UnspecializedActivityStatusPageActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unspecialized_status)

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val descTextView = findViewById<TextView>(R.id.descriptionTextView)
        val noOfParticipantsDigitTextView = findViewById<TextView>(R.id.noOfParticipantsDigitTextView)
        val imageView = findViewById<ImageView>(R.id.activityImageButton)
        val deletePostButton = findViewById<Button>(R.id.deletePostButton)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val noOfParticipants = intent.getStringExtra("noOfParticipants")
        val imageUri = intent.getStringExtra("imageUri")

        titleTextView.text = title
        descTextView.text = description
        noOfParticipantsDigitTextView.text = "0/$noOfParticipants"

        if (imageUri != null) {
            val imageUri = Uri.parse(imageUri)
            Glide.with(this)
                .load(imageUri)
                .into(imageView)
        }

        deletePostButton.setOnClickListener {
            val documentId = intent.getStringExtra("documentId")

            if (documentId != null) {
                // Delete the post from Firestore
                db.collection("unspecialized_activity_posts").document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show()

                        // Navigate back to the home page
                        val intent = Intent(this, UnspecializedActivitySelectionPageActivity::class.java)
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