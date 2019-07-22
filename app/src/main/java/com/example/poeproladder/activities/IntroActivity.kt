package com.example.poeproladder.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.poeproladder.R

class IntroActivity : AppCompatActivity() {

    lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        continueButton = findViewById<Button>(R.id.button_continue).apply {
            setOnClickListener { startIntent() }
        }
    }

    private fun startIntent() {
        startActivity(Intent(this, HostingActivity::class.java  ))
    }

}