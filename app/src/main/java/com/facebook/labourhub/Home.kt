package com.facebook.labourhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.facebook.labourhub.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Timer
import kotlin.concurrent.timerTask

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3,R.drawable.image2,R.drawable.image2) // Add your image resources here
    private var currentPage = 0
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val adapter = ImagePagerAdapter(images)
        binding.imageViewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.imageViewPager) { tab, position ->
            // You can customize the tab appearance or text here
        }.attach()

        // Set a click listener on the Available NeumorphCardView
        binding.avaiableCardview.setOnClickListener {
            // Create an Intent to navigate to the desired activity
            val intent = Intent(this@Home, Available::class.java)
            startActivity(intent)
        }

        binding.registerCardview.setOnClickListener {
            val intent = Intent(this@Home, Register::class.java)
            startActivity(intent)
        }

        // Schedule automatic image change every 5 seconds
        timer.scheduleAtFixedRate(timerTask {
            runOnUiThread {
                if (currentPage == images.size) {
                    currentPage = 0
                }
                binding.imageViewPager.currentItem = currentPage++
            }
        }, 0, 5000) // 5000 milliseconds = 5 seconds
    }
    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}