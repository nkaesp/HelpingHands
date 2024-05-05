package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.intprog.helpinghands.R
import com.intprog.helpinghands.screens.UnspecializedActivity.UnspecializedActivityStatusPageActivity

class DonationCampaignSummaryPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_campaign_summary_page)

        val backButton: ImageButton = findViewById(R.id.backTop)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val summaryImageButton: ImageButton = findViewById(R.id.summaryImageButton)
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val tagTextView: TextView = findViewById(R.id.tagTextView)
        val amountNeededTextView: TextView = findViewById(R.id.amountNeededTextView)
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
        val tag = intent.getStringExtra("tag")
        val amountNeeded = intent.getStringExtra("amountNeeded")
        val fullName = intent.getStringExtra("fullName")
        val email = intent.getStringExtra("email")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val contactMethod = intent.getStringExtra("contactMethod")

        titleTextView.text = title
        tagTextView.text = tag
        amountNeededTextView.text = amountNeeded
        fullNameTextView.text = fullName
        emailTextView.text = email
        phoneNumberTextView.text = phoneNumber
        contactMethodTextView.text = contactMethod

        val postButton = findViewById<Button>(R.id.postButton)
        postButton.setOnClickListener {
            if (!title.isNullOrEmpty() && !tag.isNullOrEmpty() && !amountNeeded.isNullOrEmpty() && !fullName.isNullOrEmpty()
                && !email.isNullOrEmpty() && !phoneNumber.isNullOrEmpty() && !contactMethod.isNullOrEmpty() && !imageUriString.isNullOrEmpty()) {
                // Start the UnspecializedActivityStatusPageActivity
                val intent = Intent(this, UnspecializedActivityStatusPageActivity::class.java).apply {
                    putExtra("title", title)
                    putExtra("tag", tag)
                    putExtra("amountNeeded", amountNeeded)
                    putExtra("fullName", fullName)
                    putExtra("email", email)
                    putExtra("phoneNumber", phoneNumber)
                    putExtra("contactMethod", contactMethod)
                    putExtra("imageUri", imageUriString)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}