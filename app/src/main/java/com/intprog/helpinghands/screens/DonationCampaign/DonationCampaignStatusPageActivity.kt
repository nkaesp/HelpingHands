package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.json.JSONException
import org.json.JSONObject

class DonationCampaignStatusPageActivity : AppCompatActivity() {

    private lateinit var paymentSheet: PaymentSheet
    private lateinit var paymentIntentClientSecret: String
    private lateinit var amount: String
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration

    private lateinit var titleTextView: TextView
    private lateinit var descTextView: TextView
    private lateinit var fullNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var contactMethodTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var deletePostButton: Button
    private lateinit var donateButton: Button
    private lateinit var currentAmountTextView: TextView
    private lateinit var noOfDonorsTextView: TextView
    private lateinit var donorsListView: ListView


    private val db = FirebaseFirestore.getInstance()
    private lateinit var donorsAdapter: ArrayAdapter<String>
    private val donorsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_campaign_status_page)

        titleTextView = findViewById(R.id.titleTextView)
        descTextView = findViewById(R.id.descTextView)
        fullNameTextView = findViewById(R.id.fullNameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView)
        contactMethodTextView = findViewById(R.id.contactMethodTextView)
        categoryTextView = findViewById(R.id.categoryTextView)
        imageView = findViewById(R.id.campaignImageView)
        deletePostButton = findViewById(R.id.deletePostButton)
        donateButton = findViewById(R.id.donateButton)
        currentAmountTextView = findViewById(R.id.currentAmountTextView)
        donorsListView = findViewById(R.id.listViewOfPeopleDonated)
        noOfDonorsTextView = findViewById(R.id.numPeopleDonated)

        // Retrieve data from intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val amountNeeded = intent.getStringExtra("amountNeeded")
        val fullName = intent.getStringExtra("fullName")
        val email = intent.getStringExtra("email")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val contactMethod = intent.getStringExtra("contactMethod")
        val category = intent.getStringExtra("category")
        val imageUri = intent.getStringExtra("imageUri")

        // Set data to views
        titleTextView.text = title
        descTextView.text = description
        fullNameTextView.text = fullName
        emailTextView.text = email
        phoneNumberTextView.text = phoneNumber
        contactMethodTextView.text = contactMethod
        categoryTextView.text = category

        if (imageUri != null) {
            val uri = Uri.parse(imageUri)
            Glide.with(this)
                .load(uri)
                .into(imageView)
        }

        donorsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, donorsList)
        donorsListView.adapter = donorsAdapter

        val documentId = intent.getStringExtra("documentId") ?: return
        db.collection("donation_campaign_posts").document(documentId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val amountsDonated = document.get("amountsDonated") as? List<Long> ?: emptyList()
                    val noOfDonors = document.getLong("noOfDonors")?.toInt() ?: 0
                    noOfDonorsTextView.text = "$noOfDonors"
                    updateCurrentAmount(amountsDonated)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch document: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        db.collection("donation_campaign_posts").document(documentId!!)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Failed to load donors count", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val noOfDonors = snapshot.getLong("noOfDonors")?.toInt() ?: 0
                    noOfDonorsTextView.text = "$noOfDonors"

                    val donors = snapshot.get("donors") as? List<String> ?: emptyList()
                    donorsList.clear() // Clear the list to prevent duplication
                    donors.forEach { donorId ->
                        db.collection("users").document(donorId)
                            .get()
                            .addOnSuccessListener { userDocument ->
                                val name = userDocument.getString("name")
                                if (name != null) {
                                    donorsList.add(name)
                                    donorsAdapter.notifyDataSetChanged()
                                }
                            }
                    }
                }
            }

        // Check if the logged-in user is the owner of the post
        val loggedInUserEmail = FirebaseAuth.getInstance().currentUser?.email
        val postOwnerEmail = email // Assuming email is the email of the post owner

        if (loggedInUserEmail == postOwnerEmail) {
            // Show delete button
            deletePostButton.visibility = View.VISIBLE
        } else {
            // Hide delete button
            deletePostButton.visibility = View.GONE
        }

        if (loggedInUserEmail == postOwnerEmail) {
            // Hide donate button
            donateButton.visibility = View.GONE
        } else {
            // Show donate button
            donateButton.visibility = View.VISIBLE
        }

        // Set click listener for delete button
        deletePostButton.setOnClickListener {
            val documentId = intent.getStringExtra("documentId")
            documentId?.let {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Post")
                builder.setMessage("Are you sure you want to delete this post?")

                // Set up the buttons
                builder.setPositiveButton("Yes") { dialog, which ->
                    // User confirmed to delete the post
                    db.collection("donation_campaign_posts").document(documentId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show()
                            // Navigate back to the home page
                            val intent = Intent(this, DonationCampaignSelectionPageActivity::class.java)
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


        donateButton.setOnClickListener {
            showDonateDialog()
        }

        // Set click listeners for other buttons
        val backTop = findViewById<ImageButton>(R.id.backTop)
        backTop.setOnClickListener {
            onBackPressed()
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

        paymentSheet = PaymentSheet(this@DonationCampaignStatusPageActivity, ::onPaymentSheetResult)

    }

    private var donationDialog: AlertDialog? = null // Declare a global variable to hold the reference to the donation dialog

    private fun showDonateDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_donate, null)
        builder.setView(dialogView)

        val confirmButton = dialogView.findViewById<Button>(R.id.confirmButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val amountEditText = dialogView.findViewById<EditText>(R.id.amountEditText)


        donationDialog = builder.create() // Assign the dialog to the global variable
        donationDialog?.show()

        confirmButton.setOnClickListener {
            if (amountEditText.text.toString().isEmpty()) {
                Toast.makeText(this@DonationCampaignStatusPageActivity, "Amount cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                val amountStr = amountEditText.text.toString()
                if (isAmountValid(amountStr)) {
                    amount = amountStr + "00"
                    getDetails()
                    donationDialog?.hide()

                }
            }
        }

        cancelButton.setOnClickListener {
            donationDialog?.dismiss()
        }
    }

    private fun isAmountValid(amountStr: String): Boolean {
        val amountValue = amountStr.toInt()
        if (amountValue < 50) {
            Toast.makeText(this@DonationCampaignStatusPageActivity, "Amount cannot be below 50", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun getDetails() {
        Fuel.post("https://us-central1-helpinghands-96b47.cloudfunctions.net/HelpingHands?amt=$amount")
            .responseString(object : Handler<String> {
                override fun success(s: String) {
                    try {
                        val result = JSONObject(s)
                        customerConfig = PaymentSheet.CustomerConfiguration(
                            result.getString("customer"),
                            result.getString("ephemeralKey")
                        )
                        paymentIntentClientSecret = result.getString("paymentIntent")
                        PaymentConfiguration.init(applicationContext, result.getString("publishableKey"))

                        runOnUiThread { showStripePaymentSheet() }

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@DonationCampaignStatusPageActivity,
                            e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun failure(fuelError: FuelError) {
                    // Handle failure
                }
            })
    }

    private fun showStripePaymentSheet() {
        val configuration = PaymentSheet.Configuration.Builder("HelpingHands")
            .customer(customerConfig)
            .allowsDelayedPaymentMethods(true)
            .build()
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            configuration
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> Toast.makeText(
                this@DonationCampaignStatusPageActivity,
                "Payment Cancelled",
                Toast.LENGTH_SHORT
            ).show()
            is PaymentSheetResult.Failed -> Toast.makeText(
                this@DonationCampaignStatusPageActivity,
                (paymentSheetResult as PaymentSheetResult.Failed).error.toString(),
                Toast.LENGTH_SHORT
            ).show()
            is PaymentSheetResult.Completed -> {
                Toast.makeText(
                    this@DonationCampaignStatusPageActivity,
                    "Payment Successful",
                    Toast.LENGTH_SHORT
                ).show()
                donationDialog?.dismiss()
                recordDonation()
            }
        }
    }

    private fun recordDonation() {
        val documentId = intent.getStringExtra("documentId") ?: return
        val donorId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val donationAmount = amount.toInt()

        db.collection("donation_campaign_posts").document(documentId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val donors = document.get("donors") as? ArrayList<String> ?: ArrayList()
                    val amountsDonated = document.get("amountsDonated") as? ArrayList<Long> ?: ArrayList()
                    val noOfDonors = document.getLong("noOfDonors")?.toInt() ?: 0

                    donors.add(donorId)
                    amountsDonated.add((donationAmount.toLong() / 100)) // Divide by 100 to reduce by 2 decimal places

                    val updates = mapOf(
                        "donors" to donors,
                        "amountsDonated" to amountsDonated,
                        "noOfDonors" to FieldValue.increment(1)
                    )

                    db.collection("donation_campaign_posts").document(documentId).update(updates)
                        .addOnSuccessListener {
                            updateCurrentAmount(amountsDonated)
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Failed to record donation: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch document: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

    }


    private fun updateCurrentAmount(amountsDonated: List<Long>) {
        val totalAmount = amountsDonated.sum()
        val amountNeeded = intent.getStringExtra("amountNeeded")
        currentAmountTextView.text = "$totalAmount  /   ₱   $amountNeeded"

        if (amountNeeded != null) {
            if (totalAmount >= amountNeeded.toInt()) {
                // Hide the join button if the total amount exceeds or equals the required amount
                donateButton.visibility = View.GONE
            }
        }
    }
}
