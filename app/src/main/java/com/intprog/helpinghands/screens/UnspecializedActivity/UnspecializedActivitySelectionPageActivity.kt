package com.intprog.helpinghands.screens.UnspecializedActivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.widget.*
import com.intprog.helpinghands.R

class UnspecializedActivitySelectionPageActivity : AppCompatActivity() {

    private lateinit var activityListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val activitiesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unspecialized_selection)

        activityListView = findViewById(R.id.activityListView)

        // Retrieve the initial list of activities from intent extras
        val initialActivities = intent.getStringArrayListExtra("activityList")
        if (initialActivities != null) {
            activitiesList.addAll(initialActivities)
        }

        // Initialize the adapter with the list of activities
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, activitiesList)
        activityListView.adapter = adapter

        // Handle click events on activity items
        activityListView.setOnItemClickListener { parent, view, position, id ->
            val selectedActivity = activitiesList[position]
            Toast.makeText(this, "Selected activity: $selectedActivity", Toast.LENGTH_SHORT).show()
            // Add your logic to handle item click, such as opening detailed view
        }
        val homeImageButton = findViewById<ImageButton>(R.id.homeImageButton)
        homeImageButton.setOnClickListener{
            val intent = Intent(this, UnspecializedActivityPostingPageActivity::class.java). apply{
            }
            startActivity(intent)
        }
    }

    // Method to add a new activity to the list
    fun addActivity(activity: String) {
        // Add the new activity to the list
        activitiesList.add(activity)
        // Notify the adapter that the dataset has changed
        adapter.notifyDataSetChanged()
    }
}


