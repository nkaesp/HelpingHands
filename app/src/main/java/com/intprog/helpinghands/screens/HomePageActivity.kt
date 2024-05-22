// HomePageActivity.kt
package com.intprog.helpinghands

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intprog.helpinghands.models.Campaign
import com.intprog.helpinghands.models.CampaignType
import com.intprog.helpinghands.screens.FeaturedOpportunitiesAdapter

class HomePageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FeaturedOpportunitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val campaigns = loadCampaigns()
        recyclerView = findViewById(R.id.featuredOpportunitiesRecyclerView)
        adapter = FeaturedOpportunitiesAdapter(campaigns)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

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

        val profileImageButton = findViewById<ImageButton>(R.id.profileImageButton)
        profileImageButton.setOnClickListener {
            val intent = Intent(this, ProfilePageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadCampaigns(): List<Campaign> {
        // Load campaigns from your data source (e.g., database, shared preferences, API)
        // Here, we'll create some sample data
        return listOf(
            Campaign(1, "Campaign 1", "Donation", "https://example.com/image1.jpg", CampaignType.DONATION),
            Campaign(2, "Campaign 2", "Volunteer", "https://example.com/image2.jpg", CampaignType.VOLUNTEER),
            // Add more campaigns
        )
    }
}
