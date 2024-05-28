package com.intprog.helpinghands.screens.UnspecializedActivity

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

class UnspecializedActivityStatusPageActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var participantsAdapter: ArrayAdapter<String>
    private val participantsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unspecialized_status)

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val descTextView = findViewById<TextView>(R.id.descriptionTextView)
        val noOfParticipantsDigitTextView = findViewById<TextView>(R.id.noOfParticipantsDigitTextView)
        val imageView = findViewById<ImageView>(R.id.activityImageButton)
        val deletePostButton = findViewById<Button>(R.id.deletePostButton)
        val joinButton = findViewById<Button>(R.id.joinButton)
        val participantsListView = findViewById<ListView>(R.id.participantsListView)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val noOfParticipants = intent.getStringExtra("noOfParticipants")?.toInt()
        val imageUri = intent.getStringExtra("imageUri")
        val email = intent.getStringExtra("email")
        val documentId = intent.getStringExtra("documentId")

        titleTextView.text = title
        descTextView.text = description

        if (imageUri != null) {
            val imageUri = Uri.parse(imageUri)
            Glide.with(this)
                .load(imageUri)
                .into(imageView)
        }

        val loggedInUserEmail = FirebaseAuth.getInstance().currentUser?.email
        val loggedInUserId = FirebaseAuth.getInstance().currentUser?.uid
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

        // Initialize ListView
        participantsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, participantsList)
        participantsListView.adapter = participantsAdapter

        // Load participants and update UI
        db.collection("unspecialized_activity_posts").document(documentId!!)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Failed to load participants count", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val currentNoOfParticipants = snapshot.getLong("currentNoOfParticipants")?.toInt() ?: 0
                    noOfParticipantsDigitTextView.text = "$currentNoOfParticipants/$noOfParticipants"

                    val participants = snapshot.get("participants") as? List<String> ?: emptyList()
                    participantsList.clear() // Clear the list to prevent duplication
                    participants.forEach { participantId ->
                        db.collection("users").document(participantId)
                            .get()
                            .addOnSuccessListener { userDocument ->
                                val name = userDocument.getString("name")
                                if (name != null) {
                                    participantsList.add(name)
                                    participantsAdapter.notifyDataSetChanged()
                                }
                            }
                    }
                }
            }

        joinButton.setOnClickListener {
            if (loggedInUserId != null && documentId != null) {
                // Build the confirmation dialog
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Join Activity")
                builder.setMessage("Are you sure you want to join this activity?")
                builder.setPositiveButton("Join") { _, _ ->
                    // User clicked Join, proceed with joining the activity
                    db.collection("unspecialized_activity_posts").document(documentId)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val currentNoOfParticipants = document.getLong("currentNoOfParticipants")?.toInt() ?: 0
                                val participants = document.get("participants") as? List<String>

                                if (participants != null && participants.contains(loggedInUserId)) {
                                    Toast.makeText(this, "You have already joined this activity", Toast.LENGTH_SHORT).show()
                                } else if (currentNoOfParticipants < noOfParticipants!!) {
                                    db.collection("unspecialized_activity_posts").document(documentId)
                                        .update(
                                            mapOf(
                                                "participants" to FieldValue.arrayUnion(loggedInUserId),
                                                "currentNoOfParticipants" to FieldValue.increment(1)
                                            )
                                        )
                                        .addOnSuccessListener {
                                            // Fetch and add the user's name to the list
                                            db.collection("users").document(loggedInUserId)
                                                .get()
                                                .addOnSuccessListener { userDocument ->
                                                    val name = userDocument.getString("name")
                                                    if (name != null && !participantsList.contains(name)) {
                                                        participantsList.add(name)
                                                        participantsAdapter.notifyDataSetChanged()
                                                        Toast.makeText(this, "Joined successfully", Toast.LENGTH_SHORT)
                                                            .show()
                                                    }
                                                }
                                        }
                                        .addOnFailureListener { exception ->
                                            Toast.makeText(this, "Failed to join: ${exception.message}", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                } else {
                                    Toast.makeText(this, "The activity is already full", Toast.LENGTH_SHORT).show()
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


        deletePostButton.setOnClickListener {
            val documentId = intent.getStringExtra("documentId")
            documentId?.let {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Post")
                builder.setMessage("Are you sure you want to delete this post?")

                // Set up the buttons
                builder.setPositiveButton("Yes") { dialog, which ->
                    // User confirmed to delete the post
                    db.collection("unspecialized_activity_posts").document(documentId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show()
                            // Navigate back to the home page
                            val intent = Intent(this, UnspecializedActivitySelectionPageActivity::class.java)
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
