package com.intprog.helpinghands

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import com.intprog.helpinghands.screens.DonationCampaign.DonationOptionPageActivity
import com.intprog.helpinghands.screens.UnspecializedActivity.UnspecializedActivitySelectionPageActivity
import com.intprog.helpinghands.screens.VolunteerCampaign.VolunteerCampaignSelectionPageActivity

class CampaignJoiningOptionsPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campaign_joining_options_page)

        val joinVolunteerCampaignButton = findViewById<Button>(R.id.joinVolunteerCampaignButton)
        joinVolunteerCampaignButton.setOnClickListener {
            val Intent = Intent( this, VolunteerCampaignSelectionPageActivity::class.java)
            startActivity(Intent)
            overridePendingTransition(0, 0)
        }

        val joinUnspecializedActivityButton = findViewById<Button>(R.id.joinUnspecializedActivityButton)
        joinUnspecializedActivityButton.setOnClickListener {
            val Intent = Intent( this, UnspecializedActivitySelectionPageActivity::class.java)
            startActivity(Intent)
            overridePendingTransition(0, 0)
        }

        val donateButton = findViewById<Button>(R.id.donateButton)
        donateButton.setOnClickListener {
            val Intent = Intent( this, DonationOptionPageActivity::class.java)
            startActivity(Intent)
            overridePendingTransition(0, 0)
        }

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish()
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
}