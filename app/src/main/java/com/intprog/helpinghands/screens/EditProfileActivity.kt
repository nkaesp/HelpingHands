package com.intprog.helpinghands

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton

class EditProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val profileButton = findViewById<ImageButton>(R.id.profileImageButton)
        profileButton.isSelected = true

        Handler(Looper.getMainLooper()).postDelayed({
            profileButton.isSelected = false
        }, 100) // Delay in milliseconds (500ms = 0.5 seconds)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Retrieve views
        val editName = findViewById<EditText>(R.id.editName)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPhone = findViewById<EditText>(R.id.editPhone)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // Load existing data to the EditText fields
        val savedName = sharedPreferences.getString("name", "")
        val savedEmail = sharedPreferences.getString("email", "")
        val savedPhone = sharedPreferences.getString("phone", "")

        editName.setText(savedName)
        editEmail.setText(savedEmail)
        editPhone.setText(savedPhone)

        // Set onClickListener for the Save button
        saveButton.setOnClickListener {
            // Save the edited data
            val editor = sharedPreferences.edit()
            editor.putString("name", editName.text.toString())
            editor.putString("email", editEmail.text.toString())
            editor.putString("phone", editPhone.text.toString())
            editor.apply()

            // Navigate back to ProfilePageActivity
            startActivity(Intent(this, ProfilePageActivity::class.java))
            finish()
            overridePendingTransition(0, 0)
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
    }
}