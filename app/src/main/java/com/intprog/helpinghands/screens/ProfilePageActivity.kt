package com.intprog.helpinghands

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class ProfilePageActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Display the registered email in TextView
        val emailTextView = findViewById<TextView>(R.id.emailTextview)
        val registeredEmail = sharedPreferences.getString("email", "")
        emailTextView.text = registeredEmail

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            onBackPressed()
        }

        val homeImageButton = findViewById<ImageButton>(R.id.homeImageButton)
        homeImageButton.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        // Adding OnClickListener to the logout button
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // Clear any session data if needed
            // For example, you might clear saved user data here
            // Start the LoginActivity
            startActivity(Intent(this, LoginPageActivity::class.java))
            // Finish the current activity to remove it from the stack
            finish()
        }
    }
}