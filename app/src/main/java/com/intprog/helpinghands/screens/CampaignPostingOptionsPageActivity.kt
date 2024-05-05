package com.intprog.helpinghands

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.intprog.helpinghands.screens.DonationCampaign.DonationCampaignPostingPageActivity
import com.intprog.helpinghands.screens.UnspecializedActivity.UnspecializedActivityPostingPageActivity
import com.intprog.helpinghands.screens.VolunteerCampaign.VolunteerCampaignPostingPageActivity

class CampaignPostingOptionsPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campaign_posting_options_page)

        val volunteerCampaignButton = findViewById<Button>(R.id.volunteerCampaignButton)
        volunteerCampaignButton.setOnClickListener {
            val Intent = Intent(this, VolunteerCampaignPostingPageActivity::class.java)
            startActivity(Intent)
        }

        val unspecializedActivityButton = findViewById<Button>(R.id.unspecializedActivityButton)
        unspecializedActivityButton.setOnClickListener {
            val Intent = Intent(this, UnspecializedActivityPostingPageActivity::class.java)
            startActivity(Intent)
        }

        val donationCampaignButton = findViewById<Button>(R.id.donationCampaignButton)
        donationCampaignButton.setOnClickListener {
            val Intent = Intent(this, DonationCampaignPostingPageActivity::class.java)
            startActivity(Intent)
        }
    }
}