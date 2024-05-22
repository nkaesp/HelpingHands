package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R

class DonationCampaignStatusPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_campaign_status_page)

        // Retrieve data from Intent extras

        val imageUriString = intent.getStringExtra("imageUri")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            val imageView = findViewById<ImageView>(R.id.campaignImageView)
            imageView.setImageURI(imageUri)
        }

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val titleInput = intent.getStringExtra("title")
        titleTextView.text = "$titleInput"

        val descTextView = findViewById<TextView>(R.id.descTextView)
        val descInput = intent.getStringExtra("description")
        descTextView.text = "$descInput"

        val amountNeededTextView = findViewById<TextView>(R.id.amountNeededTextView)
        val amountNeededInput = intent.getStringExtra("amountNeeded")
        amountNeededTextView.text = "$amountNeededInput"

        val fullNameTextView = findViewById<TextView>(R.id.fullNameTextView)
        val fullNameInput = intent.getStringExtra("fullName")
        fullNameTextView.text = "$fullNameInput"

        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val emailInput = intent.getStringExtra("email")
        emailTextView.text = "$emailInput"

        val phoneNumberTextView = findViewById<TextView>(R.id.phoneNumberTextView)
        val phoneNumberInput = intent.getStringExtra("phoneNumber")
        phoneNumberTextView.text = "$phoneNumberInput"

        val contactMethodTextView = findViewById<TextView>(R.id.contactMethodTextView)
        val contactMethodInput = intent.getStringExtra("contactMethod")
        contactMethodTextView.text = "$contactMethodInput"

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            val intent = Intent(this, DonationCampaignSelectionPageActivity::class.java)
            startActivity(intent)
            finish()
        }

        val homeImageButton = findViewById<ImageButton>(R.id.homeImageButton)
        homeImageButton.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        val profileImageButton = findViewById<ImageButton>(R.id.profileImageButton)
        profileImageButton.setOnClickListener {
            val intent = Intent(this, ProfilePageActivity::class.java)
            startActivity(intent)
        }
    }
}
