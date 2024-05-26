package com.intprog.helpinghands.screens.UnspecializedActivity

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
import com.intprog.helpinghands.model.UnspecializedActivityPost
import com.intprog.helpinghands.models.Campaign
import com.intprog.helpinghands.models.CampaignType
import com.intprog.helpinghands.screens.DonationCampaign.DonationCampaignPost
import com.intprog.helpinghands.screens.DonationCampaign.DonationCampaignSelectionPageActivity

class UnspecializedActivityStatusPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unspecialized_status)

        val currentPost = UnspecializedActivityPost(
            title = intent.getStringExtra("title") ?: "",
            noOfParticipants = intent.getStringExtra("noOfParticipants") ?: "",
            description = intent.getStringExtra("description") ?: "",
            imageUri = intent.getStringExtra("imageUri"),
            type = CampaignType.UNSPECIALIZED  // Provide a valid CampaignType value
        )
        val currentPostTitle = intent.getStringExtra("title") ?: ""

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val noOfParticipants = intent.getStringExtra("noOfParticipants")
        val imageUriString = intent.getStringExtra("imageUri")

        findViewById<TextView>(R.id.titleTextView).text = title
        findViewById<TextView>(R.id.descriptionTextView).text = description

        if (imageUriString != null && imageUriString.isNotEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            findViewById<ImageView>(R.id.activityImageButton).setImageURI(imageUri)
        }

        val noOfParticipantsDigitTextView = findViewById<TextView>(R.id.noOfParticipantsDigitTextView)
        noOfParticipantsDigitTextView.text = "0/$noOfParticipants"

        val deletePostButton = findViewById<Button>(R.id.deletePostButton)
        deletePostButton.setOnClickListener {
            deletePost(currentPost)
            deletePostFromCampaignPrefs(currentPostTitle)
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

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            onBackPressed()
            overridePendingTransition(0, 0)
        }
    }

    private fun deletePost(post: UnspecializedActivityPost?) {
        if (post == null) return

        val sharedPreferences = getSharedPreferences("UnspecializedActivityPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("posts", null)
        val type = object : TypeToken<MutableList<UnspecializedActivityPost>>() {}.type
        val posts: MutableList<UnspecializedActivityPost> = gson.fromJson(json, type) ?: mutableListOf()

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