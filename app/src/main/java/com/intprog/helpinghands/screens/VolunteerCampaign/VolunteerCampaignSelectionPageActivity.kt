package com.intprog.helpinghands.screens.VolunteerCampaign

<<<<<<< HEAD
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
=======
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
>>>>>>> e70de44f96b9fb330e58fdfb7f3e17d4e71d325c
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.R
import com.intprog.helpinghands.model.Post
import com.intprog.helpinghands.screens.VolunteerCampaign.VolunteerCampaignStatusPageActivity
import com.squareup.picasso.Picasso

class VolunteerCampaignSelectionPageActivity : AppCompatActivity() {

    private val posts = mutableListOf<Post>()
    private lateinit var listView: ListView
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_selection_page)

<<<<<<< HEAD
        listView = findViewById(R.id.volunteerCampaignListView)
        adapter = PostAdapter(this, R.layout.activity_volunteer_campaign_selection_item, posts)
        listView.adapter = adapter

        loadPostsFromSharedPreferences()

        val post = intent.getParcelableExtra<Post>("post")
        if (post != null) {
            addPost(post)
        }

=======
>>>>>>> e70de44f96b9fb330e58fdfb7f3e17d4e71d325c
        val homeImageButton = findViewById<ImageButton>(R.id.homeImageButton)
        homeImageButton.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }
<<<<<<< HEAD
    }

    fun addPost(post: Post) {
        posts.add(post)
        adapter.notifyDataSetChanged()

        savePostsToSharedPreferences()
    }

    private fun loadPostsFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("UnspecializedActivityPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("posts", null)
        val type = object : TypeToken<List<Post>>() {}.type
        val loadedPosts: List<Post>? = gson.fromJson(json, type)
        if (loadedPosts != null) {
            posts.clear()
            posts.addAll(loadedPosts)
            adapter.notifyDataSetChanged()
        }
    }

    private fun savePostsToSharedPreferences() {
        val sharedPreferences = getSharedPreferences("UnspecializedActivityPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(posts)
        editor.putString("posts", json)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private inner class PostAdapter(
        context: Context,
        resource: Int,
        objects: MutableList<Post>
    ) : ArrayAdapter<Post>(context, resource, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var itemView = convertView
            if (itemView == null) {
                val inflater = LayoutInflater.from(context)
                itemView = inflater.inflate(R.layout.activity_volunteer_campaign_selection_item, parent, false)
            }

            val post = getItem(position)
            val titleTextView = itemView!!.findViewById<TextView>(R.id.ActivityTitleTextView)
            val imageView = itemView.findViewById<ImageButton>(R.id.activityImageView)
            val viewDetailsButton = itemView.findViewById<Button>(R.id.viewDetailsButton)

            titleTextView.text = post?.title

            Picasso.get().load(post?.imageUri).into(imageView)

            viewDetailsButton.setOnClickListener {
                val intent = Intent(context, VolunteerCampaignStatusPageActivity::class.java).apply {
                    putExtra("title", post?.title)
                    putExtra("description", post?.description)
                    putExtra("imageUri", post?.imageUri)
                }
                context.startActivity(intent)
            }

            return itemView
        }

=======


        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener{
            onBackPressed()
        }
>>>>>>> e70de44f96b9fb330e58fdfb7f3e17d4e71d325c
    }
}