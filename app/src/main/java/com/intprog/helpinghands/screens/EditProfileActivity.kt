package com.intprog.helpinghands

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class EditProfileActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var profilePictureImageButton: CircleImageView
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private lateinit var saveButton: Button
    private val PICK_IMAGE_REQUEST = 1

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        profilePictureImageButton = findViewById(R.id.profilePictureImageButton)
        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editPhone = findViewById(R.id.editPhone)
        saveButton = findViewById(R.id.saveButton)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Fetch user's email from Firebase
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email
            // Set user's email in EditText and make it unmodifiable
            editEmail.setText(userEmail)
            editEmail.isEnabled = false

            // Fetch user data from Firestore
            val userId = currentUser.uid
            val userRef = db.collection("users").document(userId)
            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userData = documentSnapshot.data
                        // Populate name and phone if they exist in Firestore
                        editName.setText(userData?.get("name") as? String ?: "")
                        editPhone.setText(userData?.get("phone") as? String ?: "")
                        // Populate profile picture if imageUrl exists in Firestore
                        val imageUrl = userData?.get("imageUrl") as? String
                        if (imageUrl != null) {
                            // Load profile picture using Glide library
                            Glide.with(this)
                                .load(imageUrl)
                                .placeholder(R.drawable.person_placeholder)
                                .error(R.drawable.person_placeholder)
                                .into(profilePictureImageButton)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    // Log error message or show error toast
                }
        }

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
            onBackPressed()
            overridePendingTransition(0, 0)
        }
    }


    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    private fun saveUserData() {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            // Get the values from EditText fields
            val name = editName.text.toString()
            val phone = editPhone.text.toString()

            // Create a map with the new fields and their values including the image URL if available
            val userData = hashMapOf(
                "name" to name,
                "phone" to phone
                // Add more fields if needed
            )

            // Check if an image is selected
            if (selectedImageUri != null) {
                // Upload image to Firebase Storage
                val storageRef = storage.reference.child("profile_images").child("$userId.jpg")
                storageRef.putFile(selectedImageUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        // Get the download URL of the uploaded image
                        storageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                            // Add image URL to user data
                            userData["imageUrl"] = imageUrl.toString()

                            // Update the document with the new fields
                            val userRef = db.collection("users").document(userId)
                            userRef.set(userData, SetOptions.merge())
                                .addOnSuccessListener {
                                    // Handle success
                                    startActivity(Intent(this, ProfilePageActivity::class.java))
                                    finish()
                                    overridePendingTransition(0, 0)
                                }
                                .addOnFailureListener { e ->
                                    // Handle failure
                                    // Log error message or show error toast
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        // Handle image upload failure
                    }
            } else {
                // Update the document with the new fields if no image is selected
                val userRef = db.collection("users").document(userId)
                userRef.set(userData, SetOptions.merge())
                    .addOnSuccessListener {
                        // Handle success
                        startActivity(Intent(this, ProfilePageActivity::class.java))
                        finish()
                        overridePendingTransition(0, 0)
                    }
                    .addOnFailureListener { e ->
                        // Handle failure
                        // Log error message or show error toast
                    }
            }
        }
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Get the URI of the selected image
            selectedImageUri = data.data

            // Set the selected image to an ImageView if needed
            profilePictureImageButton.setImageURI(selectedImageUri)
        }
    }
}
