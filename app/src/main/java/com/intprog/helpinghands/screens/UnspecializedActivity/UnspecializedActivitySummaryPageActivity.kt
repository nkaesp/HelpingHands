package com.intprog.helpinghands.screens.UnspecializedActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.intprog.helpinghands.R

class UnspecializedActivitySummaryPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unspecialized_summary)

        val backButton: ImageButton = findViewById(R.id.backTop)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val summaryImageButton: ImageButton = findViewById(R.id.summaryImageButton)
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val noOfParticipantsTextView: TextView = findViewById(R.id.noOfParticipantsTextView)
        val activityDescriptionTextView: TextView = findViewById(R.id.descriptionTextView)

        val imageUriString = intent.getStringExtra("imageUri")

        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            summaryImageButton.setImageURI(imageUri)
        }

        val title = intent.getStringExtra("title")
        val noOfParticipants = intent.getStringExtra("noOfParticipants")
        val description = intent.getStringExtra("description")

        titleTextView.text = title
        noOfParticipantsTextView.text = noOfParticipants
        activityDescriptionTextView.text = description

        val postButton = findViewById<Button>(R.id.postButton)
        postButton.setOnClickListener {
            if (!title.isNullOrEmpty() && !noOfParticipants.isNullOrEmpty() && !description.isNullOrEmpty() && !imageUriString.isNullOrEmpty()) {
                val intent = Intent(this, UnspecializedActivityStatusPageActivity::class.java).apply {
                    putExtra("title", title)
                    putExtra("noOfParticipants", noOfParticipants)
                    putExtra("description", description)
                    putExtra("imageUri", imageUriString)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}