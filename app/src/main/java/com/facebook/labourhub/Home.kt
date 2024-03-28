package com.facebook.labourhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.labourhub.databinding.ActivityHomeBinding
import com.facebook.labourhub.databinding.AvailableBinding

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // Set a click listener on the Available NeumorphCardView
        binding.avaiableCardview.setOnClickListener {
            // Create an Intent to navigate to the desired activity
            val intent = Intent(this@Home, Available::class.java)
            startActivity(intent)
        }
    }
}