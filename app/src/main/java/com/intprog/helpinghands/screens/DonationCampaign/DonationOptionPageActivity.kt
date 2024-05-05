package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.intprog.helpinghands.CampaignJoiningOptionsPageActivity
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