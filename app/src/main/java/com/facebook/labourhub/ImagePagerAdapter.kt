package com.facebook.labourhub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.facebook.labourhub.databinding.ItemImagePagerBinding

class ImagePagerAdapter(private val imageUrls: List<String>) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImagePagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.imageView.load(imageUrls[position]){
            crossfade(true) // Enable crossfade for smooth transition
            // Add other options as needed
        }

    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    inner class ImageViewHolder(val binding: ItemImagePagerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageResId: Int) {
            binding.imageView.setImageResource(imageResId)
        }
    }
}

