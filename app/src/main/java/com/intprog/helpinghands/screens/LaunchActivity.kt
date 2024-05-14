package com.intprog.helpinghands

import android.content.Intent
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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

        val signInButton = findViewById<TextView>(R.id.signInButton)
        signInButton.paintFlags = signInButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        signInButton.setOnClickListener {
            val Intent = Intent(this, LoginPageActivity::class.java)
            startActivity(Intent)
        }
    }
}