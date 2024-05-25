package com.intprog.helpinghands.screens.VolunteerCampaign

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intprog.helpinghands.CampaignJoiningOptionsPageActivity
import com.intprog.helpinghands.CampaignPostingOptionsPageActivity
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R
import com.squareup.picasso.Picasso

class VolunteerCampaignSelectionPageActivity : AppCompatActivity() {

    private val posts = mutableListOf<VolunteerCampaignPost>()
    private lateinit var listView: ListView
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_selection_page)

        listView = findViewById(R.id.volunteerCampaignListView)
        adapter = PostAdapter(this, R.layout.activity_volunteer_campaign_selection_item, posts)
        listView.adapter = adapter

        loadPostsFromSharedPreferences()

        val post = intent.getParcelableExtra<VolunteerCampaignPost>("post")
        if (post != null) {
            addPost(post)
        }

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            val intent = Intent(this, CampaignJoiningOptionsPageActivity::class.java)
            startActivity(intent)
            finish()
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

    fun addPost(post: VolunteerCampaignPost) {
        posts.add(post)
        adapter.notifyDataSetChanged()

        val filterFields = linkedMapOf(
            "title" to true,
            "imageUri" to true,
            "type" to true
        ) as LinkedHashMap<String, Boolean>
        savePostsToSharedPreferences(posts, "VolunteerCampaignPrefs", filterFields)
    }

    private fun loadPostsFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("VolunteerCampaignPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("posts", null)
        val type = object : TypeToken<List<VolunteerCampaignPost>>() {}.type
        val loadedPosts: List<VolunteerCampaignPost>? = gson.fromJson(json, type)
        if (loadedPosts != null) {
            posts.clear()
            posts.addAll(loadedPosts)
            adapter.notifyDataSetChanged()
        }
    }

    private fun savePostsToSharedPreferences(posts: List<Any>, sharedPreferencesName: String, filterFields: LinkedHashMap<String, Boolean>) {
        val sharedPreferences = getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(posts)


        editor.putString("posts", json)
        editor.apply()

        // Process and save to additional shared preferences
        if (posts.isNotEmpty()) {
            val additionalSharedPreferences = getSharedPreferences("CampaignPrefs", Context.MODE_PRIVATE)
            val existingJson = additionalSharedPreferences.getString("posts", "[]")
            val type = object : TypeToken<List<LinkedHashMap<String, Any?>>>() {}.type
            val existingPosts: MutableList<LinkedHashMap<String, Any?>> = gson.fromJson(existingJson, type) ?: mutableListOf()

            val newPosts = posts.map { post ->
                val postMap = gson.fromJson(gson.toJson(post), Map::class.java) as Map<String, Any?>
                val orderedPostMap = LinkedHashMap<String, Any?>()
                filterFields.forEach { (key, _) ->
                    orderedPostMap[key] = postMap[key]
                }
                orderedPostMap
            }

            // Add only unique new posts
            for (newPost in newPosts) {
                if (!existingPosts.contains(newPost)) {
                    existingPosts.add(newPost)
                }
            }

            val combinedJson = gson.toJson(existingPosts)
            val additionalEditor = additionalSharedPreferences.edit()
            additionalEditor.putString("posts", combinedJson)
            additionalEditor.apply()
        }
    }






    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private inner class PostAdapter(
        context: Context,
        resource: Int,
        objects: MutableList<VolunteerCampaignPost>
    ) : ArrayAdapter<VolunteerCampaignPost>(context, resource, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var itemView = convertView
            if (itemView == null) {
                val inflater = LayoutInflater.from(context)
                itemView = inflater.inflate(R.layout.activity_volunteer_campaign_selection_item, parent, false)
            }

            val post = getItem(position)
            val titleTextView = itemView!!.findViewById<TextView>(R.id.campaignTitleTextView)
            val imageView = itemView.findViewById<ImageButton>(R.id.activityImageView)
            val viewDetailsButton = itemView.findViewById<Button>(R.id.viewDetailsButton)

            titleTextView.text = post?.title

            Glide.with(context)
                .load(post?.imageUri)
                .into(imageView)

            viewDetailsButton.setOnClickListener {
                val intent = Intent(context, VolunteerCampaignStatusPageActivity::class.java).apply {
                    putExtra("title", post?.title)
                    putExtra("category", post?.category)
                    putExtra("description", post?.description)
                    putExtra("startDate", post?.startDate)
                    putExtra("duration", post?.endDate)
                    putExtra("age", post?.age)
                    putExtra("location", post?.location)
                    putExtra("imageUri", post?.imageUri)
                }
                context.startActivity(intent)
                overridePendingTransition(0, 0)
            }

            return itemView
        }
    }
}