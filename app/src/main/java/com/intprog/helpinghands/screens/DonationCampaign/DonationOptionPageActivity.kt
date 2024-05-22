package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.intprog.helpinghands.CampaignJoiningOptionsPageActivity
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R

class DonationOptionPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_option_page)


        val buttonBack = findViewById<ImageButton>(R.id.backTop)
        buttonBack.setOnClickListener {
            val Intent = Intent( this, CampaignJoiningOptionsPageActivity::class.java)
            startActivity(Intent)
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

        val supportOurCauseButton = findViewById<Button>(R.id.supportOurCauseButton)
        supportOurCauseButton.setOnClickListener {
            val Intent = Intent( this, TransactionPageActivity::class.java)
            startActivity(Intent)
        }

        val fundraisingCampaignButton = findViewById<Button>(R.id.fundraisingCampaignButton)
        fundraisingCampaignButton.setOnClickListener {
            val Intent = Intent( this, DonationCampaignSelectionPageActivity::class.java)
            startActivity(Intent)
        }
    }
}