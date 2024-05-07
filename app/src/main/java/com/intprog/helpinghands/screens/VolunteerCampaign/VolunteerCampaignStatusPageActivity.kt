package com.intprog.helpinghands.screens.VolunteerCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.intprog.helpinghands.R
import com.intprog.helpinghands.screens.DonationCampaign.DonationCampaignSelectionPageActivity

class VolunteerCampaignStatusPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_status)

        // Retrieve data from intent extras
        val title = intent.getStringExtra("title")

        // Populate views with the retrieved data
        findViewById<TextView>(R.id.title).text = title

        // Retrieve and set image if available
        val imageUriString = intent.getStringExtra("imageUri")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            findViewById<ImageView>(R.id.postedpic).setImageURI(imageUri)
        }

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            val intent = Intent(this, VolunteerCampaignSelectionPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}