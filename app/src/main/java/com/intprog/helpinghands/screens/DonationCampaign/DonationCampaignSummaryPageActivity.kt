package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R
import com.intprog.helpinghands.models.CampaignType
import java.util.*

class DonationCampaignSummaryPageActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_campaign_summary_page)

        db = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            val intent = Intent(this, DonationCampaignPostingPageActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val summaryImageButton: ImageView = findViewById(R.id.imageDonationSummary)
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val amountNeededTextView: TextView = findViewById(R.id.amountNeededTextView)
        val categoryTextView: TextView = findViewById(R.id.categoryTextView)
        val fullNameTextView: TextView = findViewById(R.id.fullNameTextView)
        val emailTextView: TextView = findViewById(R.id.emailTextView)
        val phoneNumberTextView: TextView = findViewById(R.id.phoneNumberTextView)
        val contactMethodTextView: TextView = findViewById(R.id.contactMethodTextView)

        val imageUriString = intent.getStringExtra("imageUri")

        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            summaryImageButton.setImageURI(imageUri)
        }

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val amountNeeded = intent.getStringExtra("amountNeeded")
        val category = intent.getStringExtra("category")
        val fullName = intent.getStringExtra("fullName")
        val email = intent.getStringExtra("email")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val contactMethod = intent.getStringExtra("contactMethod")

        titleTextView.text = title
        descriptionTextView.text = description
        amountNeededTextView.text = amountNeeded
        categoryTextView.text = category
        fullNameTextView.text = fullName
        emailTextView.text = email
        phoneNumberTextView.text = phoneNumber
        contactMethodTextView.text = contactMethod


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


        val postButton = findViewById<Button>(R.id.btn_postNow)
        postButton.setOnClickListener {
            val post = createDonationCampaignFromUI()

            if (post != null) {
                uploadImageToStorage(post)
            } else {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createDonationCampaignFromUI(): DonationCampaignPost? {
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val amountNeededTextView: TextView = findViewById(R.id.amountNeededTextView)
        val categoryTextView: TextView = findViewById(R.id.categoryTextView)
        val fullNameTextView: TextView = findViewById(R.id.fullNameTextView)
        val emailTextView: TextView = findViewById(R.id.emailTextView)
        val phoneNumberTextView: TextView = findViewById(R.id.phoneNumberTextView)
        val contactMethodTextView: TextView = findViewById(R.id.contactMethodTextView)

        val imageUriString = intent.getStringExtra("imageUri")
        val title = titleTextView.text.toString()
        val description = descriptionTextView.text.toString()
        val amountNeeded = amountNeededTextView.text.toString()
        val category = categoryTextView.text.toString()
        val fullName = fullNameTextView.text.toString()
        val email = emailTextView.text.toString()
        val phoneNumber = phoneNumberTextView.text.toString()
        val contactMethod = contactMethodTextView.text.toString()

        return if (!title.isNullOrEmpty() && !description.isNullOrEmpty() && !amountNeeded.isNullOrEmpty() && !category.isNullOrEmpty()
            && !fullName.isNullOrEmpty() && !email.isNullOrEmpty() && !phoneNumber.isNullOrEmpty() && !contactMethod.isNullOrEmpty()
            && !imageUriString.isNullOrEmpty()) {
            DonationCampaignPost(title , description, amountNeeded, category, fullName, email, phoneNumber, contactMethod, imageUriString, CampaignType.DONATION)
        } else {
            null
        }
    }

    private fun uploadImageToStorage(post: DonationCampaignPost) {
        val imageUriString = post.imageUri

        if (imageUriString.isNullOrEmpty()) return

        val imageRef = storageRef.child("donation_campaign_post_images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(Uri.parse(imageUriString))

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully, now get the download URL
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Save post with image URL to Firestore
                val postWithImageUrl = post.copy(imageUri = uri.toString())
                saveDonationCampaignPostToFirestore(postWithImageUrl)
            }.addOnFailureListener { e ->
                // Handle any errors retrieving the download URL
                Toast.makeText(this, "Failed to retrieve image URL: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            // Handle unsuccessful uploads
            Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveDonationCampaignPostToFirestore(post: DonationCampaignPost) {
        db.collection("donation_campaign_posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Donation Campaign posted successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DonationCampaignSelectionPageActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding donation campaign: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

