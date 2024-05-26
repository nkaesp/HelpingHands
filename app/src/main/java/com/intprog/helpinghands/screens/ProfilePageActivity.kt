package com.intprog.helpinghands

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class ProfilePageActivity : AppCompatActivity() {

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val profileButton = findViewById<ImageButton>(R.id.profileImageButton)
        profileButton.isSelected = true

        // Delay to remove selection effect
        profileButton.postDelayed({
            profileButton.isSelected = false
        }, 200)

        val editProfileButton = findViewById<Button>(R.id.editProfileButton)
        editProfileButton.paintFlags = editProfileButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        // Display user details from Firestore
        displayUserDetails()

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

        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun displayUserDetails() {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val userRef = db.collection("users").document(userId)
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name") ?: "No name"
                        val email = document.getString("email") ?: "No email"
                        val phone = document.getString("phone") ?: "No phone"
                        val imageUri = document.getString("imageUrl")

                        val profilePictureImageView = findViewById<CircleImageView>(R.id.profilePictureImageView)
                        val nameTextView = findViewById<TextView>(R.id.nameTextView)
                        val emailTextView = findViewById<TextView>(R.id.emailTextView)
                        val phoneTextView = findViewById<TextView>(R.id.phoneTextView)

                        // Set user details to views
                        nameTextView.text = name
                        emailTextView.text = email
                        phoneTextView.text = phone

                        // Load profile picture using Glide library if imageUri is not null
                        if (!imageUri.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(imageUri)
                                .placeholder(R.drawable.person_placeholder)
                                .error(R.drawable.person_placeholder)
                                .into(profilePictureImageView)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    // Log error message or show error toast
                }
        }
    }

    private fun logout() {
        auth.signOut()
        val intent = Intent(this, LoginPageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }
}
