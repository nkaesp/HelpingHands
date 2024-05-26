package com.intprog.helpinghands

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EmailVerificationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var resendButton: Button
    private lateinit var checkButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verification)

        auth = Firebase.auth

        resendButton = findViewById(R.id.resendButton)
        checkButton = findViewById(R.id.checkButton)

        resendButton.setOnClickListener {
            sendVerificationEmail()
        }

        checkButton.setOnClickListener {
            checkEmailVerification()
        }
    }

    private fun sendVerificationEmail() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Verification email sent.", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("EmailVerification", "sendEmailVerification", task.exception)
                Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkEmailVerification() {
        val user = auth.currentUser
        user?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (user.isEmailVerified) {
                    Toast.makeText(this, "Email verified successfully.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginPageActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Email not verified yet.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("EmailVerification", "reload", task.exception)
                Toast.makeText(this, "Failed to reload user.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
