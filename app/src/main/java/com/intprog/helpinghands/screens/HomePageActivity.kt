package com.intprog.helpinghands

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intprog.helpinghands.model.UnspecializedActivityPost
import com.intprog.helpinghands.models.Campaign
import com.intprog.helpinghands.models.CampaignType
import com.intprog.helpinghands.screens.DonationCampaign.DonationCampaignStatusPageActivity
import com.intprog.helpinghands.screens.FeaturedOpportunitiesAdapter
import com.intprog.helpinghands.screens.UnspecializedActivity.UnspecializedActivityStatusPageActivity
import com.intprog.helpinghands.screens.VolunteerCampaign.VolunteerCampaignStatusPageActivity
import com.intprog.helpinghands.models.DonationCampaignDetails
import com.intprog.helpinghands.models.UnspecializedActivityDetails
import com.intprog.helpinghands.models.VolunteerCampaignDetails
import com.intprog.helpinghands.screens.DonationCampaign.DonationCampaignPost
import com.intprog.helpinghands.screens.VolunteerCampaign.VolunteerCampaignPost

class HomePageActivity : AppCompatActivity() {

    private val volunteerCampaignPosts = mutableListOf<VolunteerCampaignPost>()
    private val donationCampaignPosts = mutableListOf<DonationCampaignPost>()
    private val unspecializedActivityPosts = mutableListOf<UnspecializedActivityPost>()
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

        loadCampaigns()

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


    private fun loadCampaigns() {
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
        Log.d("HomePageActivity", "Navigating to status page for campaign: ${campaign.title}")
        when (campaign.type) {
            CampaignType.VOLUNTEER -> {
                val volunteerDetails = getVolunteerCampaignDetails(campaign.title)
                if (volunteerDetails != null) {
                    val intent = Intent(this, VolunteerCampaignStatusPageActivity::class.java).apply {
                        putExtra("title", volunteerDetails.title)
                        putExtra("category", volunteerDetails.category)
                        putExtra("description", volunteerDetails.description)
                        putExtra("startDate", volunteerDetails.startDate)
                        putExtra("duration", volunteerDetails.endDate)
                        putExtra("age", volunteerDetails.age)
                        putExtra("location", volunteerDetails.location)
                        putExtra("imageUri", volunteerDetails.imageUri)
                    }
                    startActivity(intent)
                } else {
                    Log.d("HomePageActivity", "Volunteer details not found for title: ${campaign.title}")
                }
            }
            CampaignType.DONATION -> {
                val donationDetails = getDonationCampaignDetails(campaign.title)
                if (donationDetails != null) {
                    val intent = Intent(this, DonationCampaignStatusPageActivity::class.java).apply {
                        putExtra("title", donationDetails.title)
                        putExtra("description", donationDetails.description)
                        putExtra("amountNeeded", donationDetails.amountNeeded)
                        putExtra("category", donationDetails.category)
                        putExtra("fullName", donationDetails.fullName)
                        putExtra("email", donationDetails.email)
                        putExtra("phoneNumber", donationDetails.phoneNumber)
                        putExtra("contactMethod", donationDetails.contactMethod)
                        putExtra("imageUri", donationDetails.imageUri)
                    }
                    startActivity(intent)
                } else {
                    Log.d("HomePageActivity", "Donation details not found for title: ${campaign.title}")
                }
            }
            CampaignType.UNSPECIALIZED -> {
                val unspecializedDetails = getUnspecializedActivityDetails(campaign.title)
                if (unspecializedDetails != null) {
                    val intent = Intent(this, UnspecializedActivityStatusPageActivity::class.java).apply {
                        putExtra("title", unspecializedDetails.title)
                        putExtra("noOfParticipants", unspecializedDetails.noOfParticipants)
                        putExtra("description", unspecializedDetails.description)
                        putExtra("imageUri", unspecializedDetails.imageUri)
                    }
                    startActivity(intent)
                } else {
                    Log.d("HomePageActivity", "Unspecialized details not found for title: ${campaign.title}")
                }
            }
        }
    }






    private fun getVolunteerCampaignDetails(title: String): VolunteerCampaignPost? {
        val sharedPrefs = getSharedPreferences("VolunteerCampaignPrefs", Context.MODE_PRIVATE)
        val json = sharedPrefs.getString("posts", null)
        if (json == null) {
            Log.d("HomePageActivity", "No details found for title: $title")
            return null
        }
        val posts: List<VolunteerCampaignPost> = Gson().fromJson(json, object : TypeToken<List<VolunteerCampaignPost>>() {}.type)
        return posts.find { it.title == title }
    }

    private fun getDonationCampaignDetails(title: String): DonationCampaignPost? {
        val sharedPrefs = getSharedPreferences("DonationCampaignPrefs", Context.MODE_PRIVATE)
        val json = sharedPrefs.getString("posts", null)
        if (json == null) {
            Log.d("HomePageActivity", "No details found for title: $title")
            return null
        }
        val posts: List<DonationCampaignPost> = Gson().fromJson(json, object : TypeToken<List<DonationCampaignPost>>() {}.type)
        return posts.find { it.title == title }
    }

    private fun getUnspecializedActivityDetails(title: String): UnspecializedActivityPost? {
        val sharedPrefs = getSharedPreferences("UnspecializedActivityPrefs", Context.MODE_PRIVATE)
        val json = sharedPrefs.getString("posts", null)
        if (json == null) {
            Log.d("HomePageActivity", "No details found for title: $title")
            return null
        }
        val posts: List<UnspecializedActivityPost> = Gson().fromJson(json, object : TypeToken<List<UnspecializedActivityPost>>() {}.type)
        return posts.find { it.title == title }
    }

}
