package com.intprog.helpinghands.screens

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.intprog.helpinghands.HomePageActivity
import com.intprog.helpinghands.ProfilePageActivity
import com.intprog.helpinghands.R
import com.intprog.helpinghands.screens.DonationCampaign.DonationOptionPageActivity
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.json.JSONException
import org.json.JSONObject

class PaymentPageActivity : AppCompatActivity() {

    private lateinit var stripeButton: Button
    private lateinit var amountEditText: EditText
    private lateinit var backTop: ImageButton
    private lateinit var homeImageButton: ImageButton
    private lateinit var profileImageButton: ImageButton

    private lateinit var paymentSheet: PaymentSheet
    private lateinit var paymentIntentClientSecret: String
    private lateinit var amount: String
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_page)

        // Initialize buttons
        stripeButton = findViewById(R.id.stripeButton)
        amountEditText = findViewById(R.id.amountEditText)
        backTop = findViewById(R.id.backTop)
        homeImageButton = findViewById(R.id.homeImageButton)
        profileImageButton = findViewById(R.id.profileImageButton)

        // Set click listeners for navigation buttons
        backTop.setOnClickListener {
            val intent = Intent(this@PaymentPageActivity, DonationOptionPageActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        homeImageButton.setOnClickListener {
            val intent = Intent(this@PaymentPageActivity, HomePageActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        profileImageButton.setOnClickListener {
            val intent = Intent(this@PaymentPageActivity, ProfilePageActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        stripeButton.setOnClickListener {
            if (amountEditText.text.toString().isEmpty()) {
                Toast.makeText(this@PaymentPageActivity, "Amount cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                val amountStr = amountEditText.text.toString()
                if (isAmountValid(amountStr)) {
                    amount = amountStr + "00"
                    getDetails()
                }
            }
        }

        paymentSheet = PaymentSheet(this@PaymentPageActivity, ::onPaymentSheetResult)
    }

    private fun isAmountValid(amountStr: String): Boolean {
        val amountValue = amountStr.toInt()
        if (amountValue < 100) {
            Toast.makeText(this@PaymentPageActivity, "Amount cannot be below 100", Toast.LENGTH_SHORT).show()
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
                            this@PaymentPageActivity,
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
                this@PaymentPageActivity,
                "Payment Cancelled",
                Toast.LENGTH_SHORT
            ).show()
            is PaymentSheetResult.Failed -> Toast.makeText(
                this@PaymentPageActivity,
                (paymentSheetResult as PaymentSheetResult.Failed).error.toString(),
                Toast.LENGTH_SHORT
            ).show()
            is PaymentSheetResult.Completed -> Toast.makeText(
                this@PaymentPageActivity,
                "Payment Successful",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
