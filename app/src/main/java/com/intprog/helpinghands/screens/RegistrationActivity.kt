package com.intprog.helpinghands

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var createAccountButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var passwordToggle: ImageView
    private lateinit var confirmPasswordToggle: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = Firebase.auth

        val backButton: ImageView = findViewById(R.id.backTop)
        backButton.setOnClickListener {
            onBackPressed()
        }

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        createAccountButton = findViewById(R.id.createAccountButton)
        passwordToggle = findViewById(R.id.passwordToggle)
        confirmPasswordToggle = findViewById(R.id.confirmPasswordToggle)
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        passwordToggle.setOnClickListener {
            togglePasswordVisibility(passwordEditText, passwordToggle)
        }

        confirmPasswordToggle.setOnClickListener {
            togglePasswordVisibility(confirmPasswordEditText, confirmPasswordToggle)
        }

        createAccountButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else if (!email.contains("@")) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (password.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                                if (verificationTask.isSuccessful) {
                                    Log.d(TAG, "Verification email sent to $email")
                                    showVerificationDialog()
                                } else {
                                    Log.e(TAG, "sendEmailVerification", verificationTask.exception)
                                    Toast.makeText(baseContext, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Registration failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
<<<<<<< HEAD
=======


                val accountsJson = sharedPreferences.getString("accounts", null)
                val type = object : TypeToken<MutableMap<String, String>>() {}.type
                val accounts: MutableMap<String, String> = if (accountsJson != null) {
                    Gson().fromJson(accountsJson, type)
                } else {
                    mutableMapOf()
                }

                if (accounts.containsKey(email)) {
                    Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show()
                } else {
                    accounts[email] = password
                    val editor = sharedPreferences.edit()
                    editor.putString("accounts", Gson().toJson(accounts))
                    editor.putString("loggedInEmail", email) // Save logged in email
                    editor.apply()
                    showRegistrationSuccessDialog()
                }
>>>>>>> d3a3487d8f9a8a0282c9cf9f55d55f4f8779a1c4
            }
        }
    }

    private fun togglePasswordVisibility(editText: EditText, toggleButton: ImageView) {
        if (editText.transformationMethod is PasswordTransformationMethod) {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            toggleButton.setImageResource(R.drawable.pw_visibility_on)
        } else {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            toggleButton.setImageResource(R.drawable.pw_visibility_off)
        }
        editText.setSelection(editText.text.length)
    }

    private fun showVerificationDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Verify Your Email")
            .setMessage("A verification email has been sent to your email address. Please verify your email to complete the registration process.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this, EmailVerificationActivity::class.java))
                finish()
            }
            .show()
    }
}
