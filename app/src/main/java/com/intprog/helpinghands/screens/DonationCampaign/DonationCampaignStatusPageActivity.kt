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

        val title = intent.getStringExtra("title")
        val tag = intent.getStringExtra("tag")
        val amountNeeded = intent.getStringExtra("amountNeeded")
        val imageUriString = intent.getStringExtra("imageUri")

        findViewById<TextView>(R.id.titleTextView).text = title
        findViewById<TextView>(R.id.tagTextView).text = tag
        findViewById<TextView>(R.id.amountNeededTextView).text = amountNeeded

        if (imageUriString != null && imageUriString.isNotEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            findViewById<ImageView>(R.id.activityImageView).setImageURI(imageUri)
        }

        val currentAmountTextView = findViewById<TextView>(R.id.currentAmountTextView)
        currentAmountTextView.text = "0"


    }
}

        //val notificationImageButton = findViewById<ImageButton>(R.id.notificationImageButton)
        //notificationImageButton.setOnClickListener {
        //    val intent = Intent(this, UnspecializedActivitySelectionPageActivity::class.java).apply {
        //        putExtra("title", title)
        //        putExtra("tag", tag)
        //        putExtra("amountNeeded", amountNeeded)
        //        putExtra("imageUri", imageUriString)
        //    }
        //    startActivity(intent)
        //}