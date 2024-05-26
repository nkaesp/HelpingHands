package com.intprog.helpinghands

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
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

    private val db = FirebaseFirestore.getInstance()
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

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        loadCampaigns()

        val createCampaignButton = findViewById<Button>(R.id.createCampaignButton)
        createCampaignButton.setOnClickListener {
            val intent = Intent(this, CampaignPostingOptionsPageActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        val joinCampaignButton = findViewById<Button>(R.id.joinCampaignButton)
        joinCampaignButton.setOnClickListener {
            val intent = Intent(this, CampaignJoiningOptionsPageActivity::class.java)
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

    private fun loadCampaigns() {
        db.collection("volunteer_campaign_posts")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val campaign = document.toObject(Campaign::class.java)
                    val updatedCampaign = campaign.copy(type = CampaignType.VOLUNTEER)
                    campaigns.add(updatedCampaign)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }


        db.collection("donation_campaign_posts")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val campaign = document.toObject(Campaign::class.java)
                    val updatedCampaign = campaign.copy(type = CampaignType.DONATION)
                    campaigns.add(updatedCampaign)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }

        db.collection("unspecialized_activity_posts")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val campaign = document.toObject(Campaign::class.java)
                    val updatedCampaign = campaign.copy(type = CampaignType.UNSPECIALIZED)
                    campaigns.add(updatedCampaign)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle any errors
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
                        putExtra("endDate", volunteerDetails.endDate)
                        putExtra("age", volunteerDetails.age)
                        putExtra("location", volunteerDetails.location)
                        putExtra("imageUri", volunteerDetails.imageUri)
                    }
                    startActivity(intent)
                    overridePendingTransition(0, 0)
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
                    overridePendingTransition(0, 0)
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
                    overridePendingTransition(0, 0)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DELETE_REQUEST_CODE && resultCode == RESULT_OK) {
            val deletedPostTitle = data?.getStringExtra("deletedPostTitle")
            if (!deletedPostTitle.isNullOrEmpty()) {
                removeDeletedPost(deletedPostTitle)
            }
        }
    }

    private fun removeDeletedPost(deletedPostTitle: String) {
        val iterator = campaigns.iterator()
        while (iterator.hasNext()) {
            val campaign = iterator.next()
            if (campaign.title == deletedPostTitle) {
                iterator.remove()
                adapter.notifyDataSetChanged()
                return
            }
        }
    }

    companion object {
        private const val DELETE_REQUEST_CODE = 123
    }

}
