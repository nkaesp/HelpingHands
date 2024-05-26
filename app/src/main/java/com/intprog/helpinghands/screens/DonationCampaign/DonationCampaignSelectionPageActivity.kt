package com.intprog.helpinghands.screens.DonationCampaign

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
import com.google.firebase.firestore.FirebaseFirestore
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R

class DonationCampaignSelectionPageActivity : AppCompatActivity() {

    private val posts = mutableListOf<DonationCampaignPost>()
    private lateinit var listView: ListView
    private lateinit var adapter: PostAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_campaign_selection_page)

        listView = findViewById(R.id.donationCampaignListView)
        adapter = PostAdapter(this, R.layout.activity_donation_campaign_selection_item, posts)
        listView.adapter = adapter

        db = FirebaseFirestore.getInstance()

        fetchPostsFromFirestore()

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
            val intent = Intent(this, DonationOptionPageActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }
    }

    private fun fetchPostsFromFirestore() {
        db.collection("donation_campaign_posts")
            .get()
            .addOnSuccessListener { result ->
                posts.clear()
                for (document in result) {
                    val post = document.toObject(DonationCampaignPost::class.java)
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
        context: Context,
        resource: Int,
        objects: MutableList<DonationCampaignPost>
    ) : ArrayAdapter<DonationCampaignPost>(context, resource, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var itemView = convertView
            if (itemView == null) {
                val inflater = LayoutInflater.from(context)
                itemView = inflater.inflate(R.layout.activity_donation_campaign_selection_item, parent, false)
            }

            val post = getItem(position)
            val titleTextView = itemView!!.findViewById<TextView>(R.id.campaignTitleTextView)
            val imageView = itemView.findViewById<ImageButton>(R.id.campaignImageView)
            val viewDetailsButton = itemView.findViewById<Button>(R.id.viewDetailsButton)

            titleTextView.text = post?.title

            Glide.with(context)
                .load(post?.imageUri)
                .into(imageView)

            viewDetailsButton.setOnClickListener {
                val post = getItem(position) // Get the post at the current position
                val intent = Intent(context, DonationCampaignStatusPageActivity::class.java).apply {
                    putExtra("title", post?.title)
                    putExtra("description", post?.description)
                    putExtra("amountNeeded", post?.amountNeeded)
                    putExtra("category", post?.category)
                    putExtra("fullName", post?.fullName)
                    putExtra("email", post?.email)
                    putExtra("phoneNumber", post?.phoneNumber)
                    putExtra("contactMethod", post?.contactMethod)
                    putExtra("imageUri", post?.imageUri)
                    putExtra("type", post?.type.toString()) // Pass campaign type
                    putExtra("documentId", post?.documentId)
                }
                context.startActivity(intent)
                overridePendingTransition(0, 0)
            }


            return itemView
        }
    }
}
