package com.intprog.helpinghands.screens

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.intprog.helpinghands.LoginPageActivity
import com.intprog.helpinghands.R

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val registrationButton = findViewById<Button>(R.id.getStartedButton)
        registrationButton.setOnClickListener {
            val Intent = Intent(this, RegistrationActivity::class.java)
            startActivity(Intent)
        }

        val signinButton = findViewById<Button>(R.id.signInButton)
        signinButton.setOnClickListener {
            val Intent = Intent(this, LoginPageActivity::class.java) //temporary ra ni
            startActivity(Intent)
        }
    }
}