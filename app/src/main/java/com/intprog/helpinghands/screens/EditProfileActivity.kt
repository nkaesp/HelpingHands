package com.intprog.helpinghands

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText

class EditProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

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
        }
    }
}