package com.intprog.helpinghands

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val createCampaignButton = findViewById<Button>(R.id.createCampaignButton)
        createCampaignButton.setOnClickListener {
            val intent = Intent(this, CampaignPostingOptionsPageActivity::class.java)
            startActivity(intent)
        }

        val joinCampaignButton = findViewById<Button>(R.id.joinCampaignButton)
        joinCampaignButton.setOnClickListener {
            val intent = Intent(this, CampaignJoiningOptionsPageActivity::class.java)
            startActivity(intent)
        }
    }
}