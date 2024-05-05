package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import com.intprog.helpinghands.R


class DonationCampaignSelectionPageActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: DonationCampaignAdapter

    private val donationCampaigns = ArrayList<DonationCampaign>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_campaign_selection_page)

        listView = findViewById(R.id.listView)
        adapter = DonationCampaignAdapter(this, donationCampaigns)
        listView.adapter = adapter

        val title = intent.getStringExtra("title")
        val imageResId = intent.getIntExtra("imageResId", 0)

        donationCampaigns.add(DonationCampaign(title, imageResId))
        adapter.notifyDataSetChanged()
    }
}
