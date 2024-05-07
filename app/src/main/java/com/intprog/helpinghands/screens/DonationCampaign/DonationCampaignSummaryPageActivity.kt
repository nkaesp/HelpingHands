package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.R

class DonationCampaignSummaryPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_campaign_summary_page)

        //Donation Details
        val titleSummary = findViewById<TextView>(R.id.titleSummary)
        val titleInput= intent.getStringExtra("titleInput")
        titleSummary.text = "Title: $titleInput"

        val descriptionSummary = findViewById<TextView>(R.id.descriptionSummary)
        val descInput= intent.getStringExtra("descInput")
        descriptionSummary.text = "Description: $descInput"

        val amountNeededSummary = findViewById<TextView>(R.id.amountNeededSummary)
        val amountNeededInput= intent.getStringExtra("amountNeededInput")
        amountNeededSummary.text = "Amount Needed: ₱$amountNeededInput"

        val categorySummary = findViewById<TextView>(R.id.categorySummary)
        val categoryInput= intent.getStringExtra("categoryInput")
        categorySummary.text = "Category: $categoryInput"

        //Contact Information
        val fullNameSummary = findViewById<TextView>(R.id.fullNameSummary)
        val fullNameInput= intent.getStringExtra("fullNameInput")
        fullNameSummary.text = "Fullname: $fullNameInput"

        val emailSummary = findViewById<TextView>(R.id.emailSummary)
        val emailInput= intent.getStringExtra("emailInput")
        emailSummary.text = "Email: $emailInput"

        val phoneNumSummary = findViewById<TextView>(R.id.phoneNumSummary)
        val phoneNumInput= intent.getStringExtra("phoneNumInput")
        phoneNumSummary.text = "Phone Number: $phoneNumInput"

        val contactMethodSummary = findViewById<TextView>(R.id.contactMethodSummary)
        val contactMethodInput= intent.getStringExtra("contactMethodInput")
        contactMethodSummary.text = "Preferred Contact Method: $contactMethodInput"

        val imageUriString = intent.getStringExtra("imageUri")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            val imageView = findViewById<ImageView>(R.id.imageDonationSummary)
            imageView.setImageURI(imageUri)
        }

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            val intent = Intent(this, DonationCampaignPostingPageActivity::class.java)
            startActivity(intent)
            finish()
        }

        val homeImageButton = findViewById<ImageButton>(R.id.homeImageButton)
        homeImageButton.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }


        val postNowButton = findViewById<Button>(R.id.btn_postNow)
        postNowButton.setOnClickListener {
            val postNowIntent = Intent(this, DonationCampaignStatusPageActivity::class.java)
            // Pass data as extras
            postNowIntent.putExtra("imageUri", imageUriString)
            postNowIntent.putExtra("titleInput", titleInput)
            postNowIntent.putExtra("descInput", descInput)
            postNowIntent.putExtra("amountNeededInput", amountNeededInput)
            // Pass any other data you want to send
            startActivity(postNowIntent)
        }

    }
}