package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
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

class DonationCampaignStatusPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_campaign_status_page)

        // Retrieve data from Intent extras
        val currentPost = DonationCampaignPost(
            title = intent.getStringExtra("title") ?: "",
            description = intent.getStringExtra("description") ?: "",
            amountNeeded = intent.getStringExtra("amountNeeded") ?: "",
            category = intent.getStringExtra("category") ?: "",
            fullName = intent.getStringExtra("fullName") ?: "",
            email = intent.getStringExtra("email") ?: "",
            phoneNumber = intent.getStringExtra("phoneNumber") ?: "",
            contactMethod = intent.getStringExtra("contactMethod") ?: "",
            imageUri = intent.getStringExtra("imageUri"),
            type = CampaignType.DONATION  // Provide a valid CampaignType value
        )
        val currentPostTitle = intent.getStringExtra("title") ?: ""


        val imageUriString = currentPost?.imageUri
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            val imageView = findViewById<ImageView>(R.id.campaignImageView)
            imageView.setImageURI(imageUri)
        }

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        titleTextView.text = currentPost?.title

        val descTextView = findViewById<TextView>(R.id.descTextView)
        descTextView.text = currentPost?.description

        val amountNeededTextView = findViewById<TextView>(R.id.amountNeededTextView)
        amountNeededTextView.text = currentPost?.amountNeeded

        val fullNameTextView = findViewById<TextView>(R.id.fullNameTextView)
        fullNameTextView.text = currentPost?.fullName

        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        emailTextView.text = currentPost?.email

        val phoneNumberTextView = findViewById<TextView>(R.id.phoneNumberTextView)
        phoneNumberTextView.text = currentPost?.phoneNumber

        val contactMethodTextView = findViewById<TextView>(R.id.contactMethodTextView)
        contactMethodTextView.text = currentPost?.contactMethod

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

    private fun deletePost(post: DonationCampaignPost?) {
        if (post == null) return

        val sharedPreferences = getSharedPreferences("DonationCampaignPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("posts", null)
        val type = object : TypeToken<MutableList<DonationCampaignPost>>() {}.type
        val posts: MutableList<DonationCampaignPost> = gson.fromJson(json, type) ?: mutableListOf()

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
