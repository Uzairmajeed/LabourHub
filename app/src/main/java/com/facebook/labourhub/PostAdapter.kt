package com.facebook.labourhub

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class PostAdapter(private var postList: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewProfile: ShapeableImageView = itemView.findViewById(R.id.imageViewProfile)
        val textViewName: MaterialTextView = itemView.findViewById(R.id.textViewName)
        val textViewAge: MaterialTextView = itemView.findViewById(R.id.textViewAge)
        val textViewMobile: MaterialTextView = itemView.findViewById(R.id.textViewMobile)
        val textViewArea: MaterialTextView = itemView.findViewById(R.id.textViewArea)
        val textViewCategory: MaterialTextView = itemView.findViewById(R.id.textViewCategory)
        val textViewAdhere: MaterialTextView = itemView.findViewById(R.id.textViewAdhere)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.available_view, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = postList[position]
        holder.textViewName.text = currentPost.username
       // holder.textViewAge.append(" " + currentPost.age)
        holder.textViewAge.text = currentPost.age
        holder.textViewMobile.text = currentPost.mobile
        holder.textViewArea.text = currentPost.area
        holder.textViewCategory.text = currentPost.category
        holder.textViewAdhere.text = currentPost.adhaar

        // Convert HTTP to HTTPS for the image URL
       // val httpsPhotoUrl = currentPost.photo.replace("http://", "https://")

        holder.imageViewProfile.load(currentPost.photo) {
            crossfade(true) // Enable crossfade for smooth image transitions
            placeholder(R.drawable.baseline_sentiment_satisfied_24) // Optional: Set a placeholder drawable while loading
            error(R.drawable.baseline_sentiment_satisfied_24) // Optional: Set an error drawable if loading fails
            listener(onError = { _, throwable ->
                Log.e("Coil", "Error loading image: ${throwable.message}")
            })
        }

    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun updateData(newList: List<Post?>) {
        postList = newList as List<Post>
        notifyDataSetChanged()
    }
}
