package com.intprog.helpinghands

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import de.hdodenhof.circleimageview.CircleImageView

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profilePictureImageButton: CircleImageView
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        profilePictureImageButton = findViewById(R.id.profilePictureImageButton)
        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editPhone = findViewById(R.id.editPhone)
        saveButton = findViewById(R.id.saveButton)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Set OnClickListener to open the gallery
        profilePictureImageButton.setOnClickListener {
            openGallery()
        }

        // Set OnClickListener to save the data
        saveButton.setOnClickListener {
            saveUserData()
        }

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            val intent = Intent(this, ProfilePageActivity::class.java)
            intent.putExtra("previousActivity", EditProfileActivity::class.java.simpleName)
            startActivity(intent)
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

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    private fun saveUserData() {
        // Get the values from EditText fields
        val name = editName.text.toString()
        val email = editEmail.text.toString()
        val phone = editPhone.text.toString()

        // Save the values in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("phone", phone)
        // Save the image URI as a string
        val imageUri = profilePictureImageButton.tag?.toString()
        editor.putString("profileImageUri", imageUri)
        editor.apply()

        // Navigate back to ProfilePageActivity
        startActivity(Intent(this, ProfilePageActivity::class.java))
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Get the URI of the selected image
            val imageUri: Uri? = data.data

            // Set the selected image as the profile picture for the ImageButton
            profilePictureImageButton.setImageURI(imageUri)
            // Store the image URI as a tag in the ImageButton
            profilePictureImageButton.tag = imageUri.toString()
        }
    }
}

