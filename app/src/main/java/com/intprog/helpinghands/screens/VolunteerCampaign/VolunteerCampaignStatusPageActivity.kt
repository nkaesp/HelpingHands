package com.intprog.helpinghands.screens.VolunteerCampaign

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R
import com.intprog.helpinghands.models.Campaign
import com.intprog.helpinghands.models.CampaignType
import com.squareup.picasso.Picasso

class VolunteerCampaignStatusPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_status)

        val currentPost = VolunteerCampaignPost(
            title = intent.getStringExtra("title") ?: "",
            category = intent.getStringExtra("category") ?: "",
            description = intent.getStringExtra("description") ?: "",
            startDate = intent.getStringExtra("startDate") ?: "",
            endDate = intent.getStringExtra("endDate") ?: "",
            age = intent.getStringExtra("age") ?: "",
            location = intent.getStringExtra("location") ?: "",
            imageUri = intent.getStringExtra("imageUri"),
            type = CampaignType.VOLUNTEER  // Provide a valid CampaignType value
        )
        val currentPostTitle = intent.getStringExtra("title") ?: ""

        // Retrieve data from intent extras
        val title = intent.getStringExtra("title")
        val category = intent.getStringExtra("category")
        val description = intent.getStringExtra("description")
        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val age = intent.getStringExtra("age")
        val location = intent.getStringExtra("location")
        val imageUriString = intent.getStringExtra("imageUri")

        // Populate views with the retrieved data
        findViewById<TextView>(R.id.title).text = title
        findViewById<TextView>(R.id.categoryTextView).text = category
        findViewById<TextView>(R.id.descTextView).text = description
        findViewById<TextView>(R.id.startDateTextView).text = startDate
        findViewById<TextView>(R.id.endDateTextView).text = endDate
        findViewById<TextView>(R.id.ageTextView).text = age
        findViewById<TextView>(R.id.locationTextView).text = location

        // Set image using Picasso
        val imageView = findViewById<ImageView>(R.id.postedpic)
        if (imageUriString != null) {
            Picasso.get().load(imageUriString).into(imageView)
        }

        val deletePostButton = findViewById<Button>(R.id.deletePostButton)
        deletePostButton.setOnClickListener {
            deletePost(currentPost)
            deletePostFromCampaignPrefs(currentPostTitle)
        }

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            onBackPressed()
            overridePendingTransition(0, 0)
        }

        val homeImageButton = findViewById<ImageButton>(R.id.homeImageButton)
        homeImageButton.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
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

    private fun deletePost(post: VolunteerCampaignPost?) {
        if (post == null) return

        val sharedPreferences = getSharedPreferences("VolunteerCampaignPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("posts", null)
        val type = object : TypeToken<MutableList<VolunteerCampaignPost>>() {}.type
        val posts: MutableList<VolunteerCampaignPost> = gson.fromJson(json, type) ?: mutableListOf()

        posts.remove(post)

        // Update shared preferences
        val editor = sharedPreferences.edit()
        val updatedJson = gson.toJson(posts)
        editor.putString("posts", updatedJson)
        editor.apply()

        // Remove the post from the list displayed in the home page
        val intent = Intent(this, HomePageActivity::class.java)
        intent.putExtra("deletedPostTitle", post.title)
        setResult(RESULT_OK, intent)

        finish()
        overridePendingTransition(0, 0)
    }

    private fun deletePostFromCampaignPrefs(postTitle: String) {
        val sharedPrefs = getSharedPreferences("CampaignPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString("posts", null)
        val type = object : TypeToken<MutableList<Campaign>>() {}.type
        val posts: MutableList<Campaign> = gson.fromJson(json, type) ?: mutableListOf()

        val postToRemove = posts.find { it.title == postTitle }
        posts.remove(postToRemove)

        val editor = sharedPrefs.edit()
        val updatedJson = gson.toJson(posts)
        editor.putString("posts", updatedJson)
        editor.apply()
    }
}
