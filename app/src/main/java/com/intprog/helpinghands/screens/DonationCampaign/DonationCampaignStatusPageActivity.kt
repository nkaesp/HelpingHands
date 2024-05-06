package com.intprog.helpinghands.screens.DonationCampaign

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
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
        val titleInput = intent.getStringExtra("titleInput")
        titleTextView.text = "$titleInput"

        val descTextView = findViewById<TextView>(R.id.descTextView)
        val descInput= intent.getStringExtra("descInput")
        descTextView.text = "$descInput"

        val amountNeededTextView =  findViewById<TextView>(R.id.amountNeededTextView)
        val amountNeededInput = intent.getStringExtra("amountNeededInput")
        amountNeededTextView.text = "$amountNeededInput"
        // Retrieve any other data you passed


        // Now you can use this data to populate your UI elements in the status page
    }

}
