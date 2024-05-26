package com.intprog.helpinghands.screens.VolunteerCampaign

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
import com.google.firebase.firestore.FirebaseFirestore
import com.intprog.helpinghands.CampaignJoiningOptionsPageActivity
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R

class VolunteerCampaignSelectionPageActivity : AppCompatActivity() {

    private val posts = mutableListOf<VolunteerCampaignPost>()
    private lateinit var listView: ListView
    private lateinit var adapter: PostAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_selection_page)

        listView = findViewById(R.id.volunteerCampaignListView)
        adapter = PostAdapter(this, R.layout.activity_volunteer_campaign_selection_item, posts)
        listView.adapter = adapter

        db = FirebaseFirestore.getInstance()

        fetchPostsFromFirestore()

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

    private fun fetchPostsFromFirestore() {
        db.collection("volunteer_campaign_posts")
            .get()
            .addOnSuccessListener { result ->
                posts.clear()
                for (document in result) {
                    val post = document.toObject(VolunteerCampaignPost::class.java)
                    posts.add(post)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle any errors here
            }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private inner class PostAdapter(
        context: AppCompatActivity,
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
                    putExtra("noOfVolunteers", post?.noOfVolunteers)
                    putExtra("startDate", post?.startDate)
                    putExtra("endDate", post?.endDate)
                    putExtra("age", post?.age)
                    putExtra("location", post?.location)
                    putExtra("email", post?.email)
                    putExtra("imageUri", post?.imageUri)
                    putExtra("documentId", post?.documentId)
                    // Add other data fields as needed
                }
                context.startActivity(intent)
                overridePendingTransition(0, 0)
            }

            return itemView
        }
    }
}
