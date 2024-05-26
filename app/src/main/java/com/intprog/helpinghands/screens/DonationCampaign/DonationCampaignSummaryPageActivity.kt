package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R
import com.intprog.helpinghands.models.CampaignType

class DonationCampaignSummaryPageActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_campaign_summary_page)

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
            val post = createDonationPostFromUI()

            if (post != null) {
                saveDonationPostToFirestore(post)
            } else {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createDonationPostFromUI(): DonationPost? {
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
            DonationPost(title, description, amountNeeded, category, fullName, email, phoneNumber, contactMethod, imageUriString)
        } else {
            null
        }
    }

    private fun saveDonationPostToFirestore(post: DonationPost) {
        db.collection("donationPosts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Donation post added successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DonationCampaignSelectionPageActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding donation post: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

