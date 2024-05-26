package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R

class DonationCampaignStatusPageActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var descTextView: TextView
    private lateinit var amountNeededTextView: TextView
    private lateinit var fullNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var contactMethodTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var deletePostButton: Button
    private lateinit var donateButton: Button


    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_campaign_status_page)

        titleTextView = findViewById(R.id.titleTextView)
        descTextView = findViewById(R.id.descTextView)
        amountNeededTextView = findViewById(R.id.amountNeededTextView)
        fullNameTextView = findViewById(R.id.fullNameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView)
        contactMethodTextView = findViewById(R.id.contactMethodTextView)
        categoryTextView = findViewById(R.id.categoryTextView)
        imageView = findViewById(R.id.campaignImageView)
        deletePostButton = findViewById(R.id.deletePostButton)
        donateButton = findViewById(R.id.donateButton)

        // Retrieve data from intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val amountNeeded = intent.getStringExtra("amountNeeded")
        val fullName = intent.getStringExtra("fullName")
        val email = intent.getStringExtra("email")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val contactMethod = intent.getStringExtra("contactMethod")
        val category = intent.getStringExtra("category")
        val imageUri = intent.getStringExtra("imageUri")

        // Set data to views
        titleTextView.text = title
        descTextView.text = description
        amountNeededTextView.text = amountNeeded
        fullNameTextView.text = fullName
        emailTextView.text = email
        phoneNumberTextView.text = phoneNumber
        contactMethodTextView.text = contactMethod
        categoryTextView.text = category

        if (imageUri != null) {
            val uri = Uri.parse(imageUri)
            Glide.with(this)
                .load(uri)
                .into(imageView)
        }

        // Check if the logged-in user is the owner of the post
        val loggedInUserEmail = FirebaseAuth.getInstance().currentUser?.email
        val postOwnerEmail = email // Assuming email is the email of the post owner

        if (loggedInUserEmail == postOwnerEmail) {
            // Show delete button
            deletePostButton.visibility = View.VISIBLE
        } else {
            // Hide delete button
            deletePostButton.visibility = View.GONE
        }

        if (loggedInUserEmail == postOwnerEmail) {
            // Hide donate button
            donateButton.visibility = View.GONE
        } else {
            // Show donate button
            donateButton.visibility = View.VISIBLE
        }

        // Set click listener for delete button
        deletePostButton.setOnClickListener {
            val documentId = intent.getStringExtra("documentId")
            documentId?.let {
                // Delete the post from Firestore
                db.collection("donation_campaign_posts").document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show()
                        // Navigate back to the home page
                        val intent = Intent(this, DonationCampaignSelectionPageActivity::class.java)
                        startActivity(intent)
                        finish()
                        overridePendingTransition(0, 0)
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Failed to delete post: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Set click listeners for other buttons
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
    }
}
