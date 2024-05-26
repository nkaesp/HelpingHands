package com.intprog.helpinghands

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginPageActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        auth = FirebaseAuth.getInstance()

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
            // Implement forgot password functionality if needed
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Check if the email is verified
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        // Email is verified, proceed to home page
                        startActivity(Intent(this, HomePageActivity::class.java))
                        finish()
                    } else {
                        // Email is not verified, show a message
                        Toast.makeText(baseContext, "Please verify your email address.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed. ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
