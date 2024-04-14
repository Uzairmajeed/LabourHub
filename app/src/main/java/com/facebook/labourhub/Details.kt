package com.facebook.labourhub

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.util.Linkify
import coil.load
import com.facebook.labourhub.databinding.ActivityDetailsBinding

class Details : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.backButton.setOnClickListener {
            finish()
        }

        val postDetails = intent.getSerializableExtra("postDetails") as Post
        updateUI(postDetails)
    }

    private fun updateUI(post: Post) {
        // Update TextViews with post details
        binding.detailstextViewName.text = "Name: ${post.username}"
        binding.detailstextViewAge.text = "Age: ${post.age}"
        binding.detailstextViewCategory.text = "Category: ${post.category}"
        binding. textViewArea.text = "Area: ${post.area}"
        binding. textViewAdhere.text = "Adhere: ${post.adhaar}"
        binding.textViewMobile.apply {
            text = "Contact At: ${post.mobile}"
            // Make the mobile number clickable
            Linkify.addLinks(this, Linkify.PHONE_NUMBERS)
            // Set a click listener to handle the click on the phone number
            setOnClickListener {
                // Create an intent to open the dialer with the phone number
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${post.mobile}")
                startActivity(intent)
            }
        }
        // You can also load the image using Coil or another library
        binding.detailsimageView.load(post.photo) {
            crossfade(true)
            placeholder(R.drawable.baseline_sentiment_satisfied_24) // Placeholder image while loading
            error(R.drawable.baseline_sentiment_satisfied_24) // Error image if loading fails
        }
    }
}
