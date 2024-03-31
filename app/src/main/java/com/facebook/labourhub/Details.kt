package com.facebook.labourhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.facebook.labourhub.databinding.ActivityDetailsBinding

class Details : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.textViewMobile.text = "Contact At: ${post.mobile}"
        // You can also load the image using Coil or another library
        binding.detailsimageView.load(post.photo) {
            crossfade(true)
            placeholder(R.drawable.baseline_sentiment_satisfied_24) // Placeholder image while loading
            error(R.drawable.baseline_sentiment_satisfied_24) // Error image if loading fails
        }
    }
}
