package com.intprog.helpinghands

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginPageActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                signIn(email, password)
            }
        }

        val passwordVisibilityToggle = findViewById<ImageView>(R.id.passwordVisibilityToggle)
        passwordVisibilityToggle.setOnClickListener {
            val isPasswordVisible = passwordEditText.transformationMethod == null
            if (isPasswordVisible) {
                // Hide password
                passwordVisibilityToggle.setImageResource(R.drawable.pw_visibility_off)
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                // Show password
                passwordVisibilityToggle.setImageResource(R.drawable.pw_visibility_on)
                passwordEditText.transformationMethod = null
            }
        }

        val signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        val forgotPasswordButton = findViewById<Button>(R.id.forgotPasswordButton)
        forgotPasswordButton.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        firestore.collection("users").document(user.uid)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document != null && document.exists()) {
                                    val isVerified = document.getBoolean("verified") ?: false
                                    if (isVerified) {
                                        startActivity(Intent(this, HomePageActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(baseContext, "Please verify your email address before logging in.", Toast.LENGTH_SHORT).show()
                                        auth.signOut()  // Sign out the user since the account is not verified
                                    }
                                } else {
                                    Toast.makeText(baseContext, "User data not found.", Toast.LENGTH_SHORT).show()
                                    auth.signOut()
                                }
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(baseContext, "Error accessing user data: ${exception.message}", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                            }
                    }
                } else {
                    Toast.makeText(baseContext, "Authentication failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showForgotPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.forgot_password_dialog, null)
        val emailEditText = dialogView.findViewById<EditText>(R.id.emailEditText)

        builder.setView(dialogView)
            .setTitle("Forgot Password")
            .setPositiveButton("Send Email") { dialogInterface: DialogInterface, i: Int ->
                val email = emailEditText.text.toString()
                sendPasswordResetEmail(email)
            }
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                // Do nothing
            }
            .show()
    }

    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}