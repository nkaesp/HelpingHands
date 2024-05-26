package com.intprog.helpinghands

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class ProfilePageActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        val profileButton = findViewById<ImageButton>(R.id.profileImageButton)
        profileButton.isSelected = true

        Handler(Looper.getMainLooper()).postDelayed({
            profileButton.isSelected = false
        }, 100) // Delay in milliseconds (500ms = 0.5 seconds)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val editProfileButton = findViewById<Button>(R.id.editProfileButton)
        editProfileButton.paintFlags = editProfileButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        editProfileButton.setOnClickListener{
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }


        // Display user details
        displayUserDetails()

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            val previousActivity = intent.getStringExtra("previousActivity")

            if (previousActivity == EditProfileActivity::class.java.simpleName) {
                // Previous activity was EditProfileActivity, so go to HomePageActivity
                val intent = Intent(this, HomePageActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(0, 0)
            } else {
                // Previous activity was not EditProfileActivity, so trigger onBackPressed()
                onBackPressed()
                overridePendingTransition(0, 0)
            }
        }


        val homeImageButton = findViewById<ImageButton>(R.id.homeImageButton)
        homeImageButton.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    private fun displayUserDetails() {
        val profilePictureImageView = findViewById<CircleImageView>(R.id.profilePictureImageView)
        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val phoneTextView = findViewById<TextView>(R.id.phoneTextView)

        // Retrieve user details from SharedPreferences
        val name = sharedPreferences.getString("name", "No name")
        val email = sharedPreferences.getString("email", "No email")
        val phone = sharedPreferences.getString("phone", "No phone")
        val imageUri = sharedPreferences.getString("profileImageUri", null)

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


