package com.intprog.helpinghands.screens.UnspecializedActivity

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
import com.intprog.helpinghands.model.UnspecializedActivityPost

class UnspecializedActivitySelectionPageActivity : AppCompatActivity() {

    private val posts = mutableListOf<UnspecializedActivityPost>()
    private lateinit var listView: ListView
    private lateinit var adapter: PostAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unspecialized_selection)

        listView = findViewById(R.id.unspecializedActivityListView)
        adapter = PostAdapter(this, R.layout.activity_unspecialized_selection_item, posts)
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
            val intent = Intent(this, CampaignJoiningOptionsPageActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }
    }

    private fun fetchPostsFromFirestore() {
        db.collection("unspecialized_activity_posts")
            .get()
            .addOnSuccessListener { result ->
                posts.clear()
                for (document in result) {
                    val post = document.toObject(UnspecializedActivityPost::class.java)
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
        objects: MutableList<UnspecializedActivityPost>
    ) : ArrayAdapter<UnspecializedActivityPost>(context, resource, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var itemView = convertView
            if (itemView == null) {
                val inflater = LayoutInflater.from(context)
                itemView = inflater.inflate(R.layout.activity_unspecialized_selection_item, parent, false)
            }

            val post = getItem(position)
            val titleTextView = itemView!!.findViewById<TextView>(R.id.ActivityTitleTextView)
            val imageView = itemView.findViewById<ImageButton>(R.id.activityImageView)
            val viewDetailsButton = itemView.findViewById<Button>(R.id.viewDetailsButton)

            titleTextView.text = post?.title

            Glide.with(context)
                .load(post?.imageUri)
                .into(imageView)

            viewDetailsButton.setOnClickListener {
                val intent = Intent(context, UnspecializedActivityStatusPageActivity::class.java).apply {
                    putExtra("title", post?.title)
                    putExtra("description", post?.description)
                    putExtra("noOfParticipants", post?.noOfParticipants)
                    putExtra("imageUri", post?.imageUri)
                    putExtra("type", post?.type.toString()) // Pass campaign type
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
