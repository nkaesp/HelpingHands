package com.intprog.helpinghands.screens.DonationCampaign

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import com.intprog.helpinghands.R

class DonationCampaignSelectionPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_campaign_selection_page)

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener{
            onBackPressed()
        }

    }
}
