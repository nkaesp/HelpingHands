package com.intprog.helpinghands.screens.DonationCampaign

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.InputFilter
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.intprog.helpinghands.CampaignPostingOptionsPageActivity
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R

class DonationCampaignPostingPageActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_posting_page)

        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            showConfirmationDialog()
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



        val title = findViewById<EditText>(R.id.title)
        title.filters = arrayOf(InputFilter.LengthFilter(30))
        val desc = findViewById<EditText>(R.id.desc)
        val amountNeeded = findViewById<EditText>(R.id.amountNeeded)
        amountNeeded.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        amountNeeded.filters = arrayOf(InputFilter.LengthFilter(7))
        val category = findViewById<EditText>(R.id.category)
        category.filters = arrayOf(InputFilter.LengthFilter(20))

        val fullName = findViewById<EditText>(R.id.fullName)
        fullName.filters = arrayOf(InputFilter.LengthFilter(30))
        val email = findViewById<EditText>(R.id.email)
        // Set user's email in EditText and make it unmodifiable
        email.setText(FirebaseAuth.getInstance().currentUser?.email)
        email.isEnabled = false

        val phoneNum = findViewById<EditText>(R.id.phoneNum)
        phoneNum.inputType = InputType.TYPE_CLASS_NUMBER
        phoneNum.filters = arrayOf(InputFilter.LengthFilter(11))
        val contactMethod = findViewById<EditText>(R.id.contactMethod)
        contactMethod.filters = arrayOf(InputFilter.LengthFilter(5))

        val btnUploadImage = findViewById<ImageButton>(R.id.btnUploadImage)
        btnUploadImage.setOnClickListener {
            openGallery()
        }

        val btn_continue = findViewById<Button>(R.id.btn_continue)
        btn_continue.setOnClickListener {
            if (imageUri == null) {
                Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val theTitle = title.text.toString()
            val theDesc = desc.text.toString()
            val theAmountNeeded = amountNeeded.text.toString()
            val theCategory = category.text.toString()
            val theFullName = fullName.text.toString()
            val theEmail = email.text.toString()
            val thePhoneNum = phoneNum.text.toString()
            val theContactMethod = contactMethod.text.toString()

            val isValidContactMethod = isValidContactMethod(theContactMethod)
            if (!isValidContactMethod) {
                Toast.makeText(this, "Preferred contact method must be email or phone", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (theTitle.isNotEmpty() && theDesc.isNotEmpty() && theAmountNeeded.isNotEmpty() &&
                theCategory.isNotEmpty() && theFullName.isNotEmpty() &&
                thePhoneNum.isNotEmpty() && theContactMethod.isNotEmpty()
            ) {
                val intent = Intent(this, DonationCampaignSummaryPageActivity::class.java).apply {
                    putExtra("title", theTitle)
                    putExtra("description", theDesc)
                    putExtra("amountNeeded", theAmountNeeded)
                    putExtra("category", theCategory)
                    putExtra("fullName", theFullName)
                    putExtra("email", theEmail)
                    putExtra("phoneNumber", thePhoneNum)
                    putExtra("contactMethod", theContactMethod)
                    putExtra("imageUri", imageUri.toString())
                }
                startActivity(intent)
                overridePendingTransition(0, 0)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
        overridePendingTransition(0, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            val btnUploadImage = findViewById<ImageButton>(R.id.btnUploadImage)
            btnUploadImage.setImageURI(imageUri)
        }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure you want discard your post?")
            .setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
                overridePendingTransition(0, 0)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun isValidContactMethod(contactMethod: String): Boolean {
        val trimmedContactMethod = contactMethod.trim().toLowerCase()
        return trimmedContactMethod == "email" || trimmedContactMethod == "phone"
    }
}