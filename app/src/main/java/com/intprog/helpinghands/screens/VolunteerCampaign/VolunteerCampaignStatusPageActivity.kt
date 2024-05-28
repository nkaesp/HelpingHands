package com.intprog.helpinghands.screens.VolunteerCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R

class VolunteerCampaignStatusPageActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var volunteersAdapter: ArrayAdapter<String>
    private val volunteersList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_campaign_status)

        val titleTextView = findViewById<TextView>(R.id.title)
        val categoryTextView = findViewById<TextView>(R.id.categoryTextView)
        val descTextView = findViewById<TextView>(R.id.descTextView)
        val startDateTextView = findViewById<TextView>(R.id.startDateTextView)
        val endDateTextView = findViewById<TextView>(R.id.endDateTextView)
        val ageTextView = findViewById<TextView>(R.id.ageTextView)
        val locationTextView = findViewById<TextView>(R.id.locationTextView)
        val imageView = findViewById<ImageView>(R.id.postedpic)
        val deletePostButton = findViewById<Button>(R.id.deletePostButton)
        val joinButton = findViewById<Button>(R.id.joinButton)
        val volunteersListView = findViewById<ListView>(R.id.peopleWhoVolunteeredListView)
        val totalNumTextView = findViewById<TextView>(R.id.totalNum)

        val title = intent.getStringExtra("title")
        val category = intent.getStringExtra("category")
        val description = intent.getStringExtra("description")
        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val age = intent.getStringExtra("age")
        val location = intent.getStringExtra("location")
        val imageUri = intent.getStringExtra("imageUri")
        val email = intent.getStringExtra("email")
        val documentId = intent.getStringExtra("documentId")
        val noOfVolunteers = intent.getStringExtra("noOfVolunteers")

        titleTextView.text = title
        categoryTextView.text = category
        descTextView.text = description
        startDateTextView.text = startDate
        endDateTextView.text = endDate
        ageTextView.text = age
        locationTextView.text = location
//        totalNumTextView.text = "${volunteersList.size}/$noOfVolunteers"


        if (imageUri != null) {
            Glide.with(this)
                .load(Uri.parse(imageUri))
                .into(imageView)
        }

        // Initialize ListView
        volunteersAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, volunteersList)
        volunteersListView.adapter = volunteersAdapter

        // Load volunteers and update UI
        db.collection("volunteer_campaign_posts").document(documentId!!)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Failed to load volunteers count", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val currentNoOfVolunteers = snapshot.getLong("currentNoOfVolunteers")?.toInt() ?: 0
                    totalNumTextView.text = "$currentNoOfVolunteers/$noOfVolunteers"

                    val volunteers = snapshot.get("volunteers") as? List<String> ?: emptyList()
                    volunteersList.clear() // Clear the list to prevent duplication
                    volunteers.forEach { volunteerId ->
                        db.collection("users").document(volunteerId)
                            .get()
                            .addOnSuccessListener { userDocument ->
                                val name = userDocument.getString("name")
                                if (name != null) {
                                    volunteersList.add(name)
                                    volunteersAdapter.notifyDataSetChanged()
                                }
                            }
                    }
                }
            }


        val loggedInUserEmail = FirebaseAuth.getInstance().currentUser?.email
        val postOwnerEmail = email

        if (loggedInUserEmail == postOwnerEmail) {
            // Show delete button
            deletePostButton.visibility = View.VISIBLE
        } else {
            // Hide delete button
            deletePostButton.visibility = View.GONE
        }

        if (loggedInUserEmail == postOwnerEmail) {
            // Hide join button
            joinButton.visibility = View.GONE
        } else {
            // Show join button
            joinButton.visibility = View.VISIBLE
        }

        deletePostButton.setOnClickListener {
            val documentId = intent.getStringExtra("documentId")
            documentId?.let {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Post")
                builder.setMessage("Are you sure you want to delete this post?")

                // Set up the buttons
                builder.setPositiveButton("Yes") { dialog, which ->
                    // User confirmed to delete the post
                    db.collection("volunteer_campaign_posts").document(documentId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show()
                            // Navigate back to the home page
                            val intent = Intent(this, VolunteerCampaignSelectionPageActivity::class.java)
                            startActivity(intent)
                            finish()
                            overridePendingTransition(0, 0)
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Failed to delete post: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }

                builder.setNegativeButton("No") { dialog, which ->
                    // User cancelled the dialog
                    dialog.dismiss()
                }

                builder.show()
            }
        }


        joinButton.setOnClickListener {
            val loggedInUserId = FirebaseAuth.getInstance().currentUser?.uid
            if (loggedInUserId != null && documentId != null) {
                // Build the confirmation dialog
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Join Campaign")
                builder.setMessage("Are you sure you want to join this campaign?")
                builder.setPositiveButton("Join") { _, _ ->
                    // User clicked Join, proceed with joining the campaign
                    db.collection("volunteer_campaign_posts").document(documentId)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val currentNoOfVolunteers = document.getLong("currentNoOfVolunteers")?.toInt() ?: 0
                                val volunteers = document.get("volunteers") as? List<String>

                                if (volunteers != null && volunteers.contains(loggedInUserId)) {
                                    Toast.makeText(this, "You have already joined this campaign", Toast.LENGTH_SHORT).show()
                                } else if (currentNoOfVolunteers < noOfVolunteers?.toInt()!!) {
                                    db.collection("volunteer_campaign_posts").document(documentId)
                                        .update(
                                            mapOf(
                                                "volunteers" to FieldValue.arrayUnion(loggedInUserId),
                                                "currentNoOfVolunteers" to FieldValue.increment(1)
                                            )
                                        )
                                        .addOnSuccessListener {
                                            // Fetch and add the user's name to the list
                                            db.collection("users").document(loggedInUserId)
                                                .get()
                                                .addOnSuccessListener { userDocument ->
                                                    val name = userDocument.getString("name")
                                                    if (name != null && !volunteersList.contains(name)) {
                                                        volunteersList.add(name)
                                                        volunteersAdapter.notifyDataSetChanged()
                                                        totalNumTextView.text = "${volunteersList.size}"
                                                        Toast.makeText(this, "Joined successfully", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                        }
                                        .addOnFailureListener { exception ->
                                            Toast.makeText(this, "Failed to join: ${exception.message}", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                    Toast.makeText(this, "The campaign is already full", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Failed to get document: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                builder.setNegativeButton("Cancel") { _, _ ->
                    // User clicked Cancel, do nothing
                }
                // Show the dialog
                builder.show()
            }
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
}
