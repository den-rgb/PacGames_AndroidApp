package com.example.pacgamesandroid.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pacgamesandroid.R
import java.util.*

// Create a new activity for the splash screen
class splashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout for the splash screen
        setContentView(R.layout.splash)

        // Start a timer to switch to the main activity after a few seconds
        val timer = Timer()

        timer.schedule(object : TimerTask() {
            override fun run() {
                startActivity(Intent(this@splashActivity, GameListActivity::class.java))
                finish()
            }
        }, 3000)
    }
}