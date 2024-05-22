// HomePageActivity.kt
package com.intprog.helpinghands

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intprog.helpinghands.models.Campaign
import com.intprog.helpinghands.models.CampaignType
import com.intprog.helpinghands.screens.DonationCampaign.DonationCampaignStatusPageActivity
import com.intprog.helpinghands.screens.FeaturedOpportunitiesAdapter
import com.intprog.helpinghands.screens.UnspecializedActivity.UnspecializedActivityStatusPageActivity
import com.intprog.helpinghands.screens.VolunteerCampaign.VolunteerCampaignPost
import com.intprog.helpinghands.screens.VolunteerCampaign.VolunteerCampaignStatusPageActivity
import org.json.JSONArray
import org.json.JSONObject

class HomePageActivity : AppCompatActivity() {

    private val campaigns = mutableListOf<Campaign>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FeaturedOpportunitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)



        recyclerView = findViewById(R.id.featuredOpportunitiesRecyclerView)
        adapter = FeaturedOpportunitiesAdapter(campaigns) { campaign ->
            navigateToStatusPage(campaign)
        }

        loadCampaignsFromPreferences()

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

    private fun loadCampaignsFromPreferences() {
        val sharedPrefs = getSharedPreferences("CampaignPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString("posts", null)
        val type = object : TypeToken<List<Campaign>>() {}.type
        val loadedPosts: List<Campaign>? = gson.fromJson(json, type)
        if (loadedPosts != null) {
            campaigns.clear()
            campaigns.addAll(loadedPosts)
            adapter.notifyDataSetChanged()
        }
    }

    private fun navigateToStatusPage(campaign: Campaign) {
        val intent = when (campaign.type) {
            CampaignType.VOLUNTEER -> Intent(this, VolunteerCampaignStatusPageActivity::class.java)
            CampaignType.DONATION -> Intent(this, DonationCampaignStatusPageActivity::class.java)
            CampaignType.UNSPECIALIZED -> Intent(this, UnspecializedActivityStatusPageActivity::class.java)
        }
        intent.putExtra("campaignTitle", campaign.title)
        startActivity(intent)
    }
}