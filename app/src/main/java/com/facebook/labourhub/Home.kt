package com.facebook.labourhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.labourhub.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import org.json.JSONException
import org.json.JSONObject
import java.util.Timer
import kotlin.concurrent.timerTask

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private val imageUrlList = mutableListOf<String>() // List to store image URLs
    private var currentPage = 0
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // Initialize Firebase Remote Config
        firebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600 // Fetch interval in seconds (e.g., every hour)
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        // Fetch Remote Config values
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val imagesJson = firebaseRemoteConfig.getString("images") // Use the correct key "images" to fetch the JSON object
                    Log.d("ImageUrlJson", "Json: $imagesJson")
                    try {
                        val imageUrlsArray = JSONObject(imagesJson).getJSONArray("image_urls") // Access the "image_urls" array inside the "images" JSON object
                        for (i in 0 until imageUrlsArray.length()) {
                            val imageUrl = imageUrlsArray.getString(i)
                            imageUrlList.add(imageUrl)
                        }
                        Log.d("ImageUrlListSize", "Size: ${imageUrlList.size}") // Log the size of imageUrlList
                        setupViewPager(imageUrlList)
                    } catch (e: JSONException) {
                        Log.e("JsonParsingError", "Error parsing JSON: ${e.message}")
                    }
                } else {
                    Log.e("RemoteConfigError", "Failed to fetch Remote Config values")
                    // Handle errors while fetching Remote Config values
                }
            }

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

        binding.updateCardview.setOnClickListener {
            val intent = Intent(this@Home, Update::class.java)
            startActivity(intent)
        }

        // Schedule automatic image change every 5 seconds
        timer.scheduleAtFixedRate(timerTask {
            runOnUiThread {
                if (currentPage == imageUrlList.size) {
                    currentPage = 0
                }
                binding.imageViewPager.currentItem = currentPage++
            }
        }, 0, 3000) // 5000 milliseconds = 3 seconds
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    private fun setupViewPager(imageUrls: List<String>) {
        val adapter = ImagePagerAdapter(imageUrls)
        binding.imageViewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.imageViewPager) { tab, position ->
            // You can customize the tab appearance or text here
        }.attach()
    }
}
