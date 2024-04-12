package com.facebook.labourhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.facebook.labourhub.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private  lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handshake= binding.handshake
        handshake.playAnimation()

        // Set a delay using Handler to keep the splash screen for at least 5 seconds
        // Set a delay using Handler to keep the splash screen for at least 5 seconds
        Handler().postDelayed(Runnable {
            // Intent to start the MainActivity
            val intent = Intent(this@SplashScreen, Home::class.java)
            startActivity(intent)
            finish() // Close the SplashScreen activity so the user cannot go back to it
        }, 3000) // 5000 milliseconds (5 seconds)

    }
}