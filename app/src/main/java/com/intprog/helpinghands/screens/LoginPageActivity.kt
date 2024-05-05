package com.intprog.helpinghands

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.intprog.helpinghands.screens.RegistrationActivity

class LoginPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val forgotPasswordButton = findViewById<Button>(R.id.forgotPasswordButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

//        forgotPasswordButton.setOnClickListener {
//            Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, ForgotPasswordActivity::class.java)
//            startActivity(intent)
//        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            // Validate email and password, perform login logic
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }


    }
}