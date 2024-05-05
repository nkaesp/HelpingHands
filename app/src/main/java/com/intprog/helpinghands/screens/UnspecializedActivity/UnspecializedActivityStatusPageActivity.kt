package com.intprog.helpinghands.screens.UnspecializedActivity

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.intprog.helpinghands.R

class UnspecializedActivityStatusPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unspecialized_status)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val noOfParticipants = intent.getStringExtra("noOfParticipants")
        val imageUriString = intent.getStringExtra("imageUri")

        findViewById<TextView>(R.id.titleTextView).text = title
        findViewById<TextView>(R.id.descriptionTextView).text = description

        if (imageUriString != null && imageUriString.isNotEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            findViewById<ImageButton>(R.id.activityImageButton).setImageURI(imageUri)
        }

        val noOfParticipantsDigitTextView = findViewById<TextView>(R.id.noOfParticipantsDigitTextView)
        noOfParticipantsDigitTextView.text = " 0/$noOfParticipants"


    }
}