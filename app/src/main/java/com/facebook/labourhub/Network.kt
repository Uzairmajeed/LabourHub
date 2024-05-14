package com.facebook.labourhub

import android.util.Log
import com.google.gson.Gson
import org.json.JSONArray

class Network {
    private val getRepository = GetRepository()

    suspend fun fetchData(): List<Post?> {
        return try {
            val responseBody = getRepository.getFromFirebaseStorage()
            Log.e("Network", responseBody.toString())

            val posts = mutableListOf<Post?>()

            responseBody?.let {
                val jsonArray = JSONArray(responseBody) // Assuming the response is in JSON array format

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val post = try {
                        Gson().fromJson(jsonObject.toString(), Post::class.java)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("Network", "Exception: ${e.message}")
                        null
                    }
                    post?.let { posts.add(it) } // Add non-null posts to the list
                }
            }

            posts
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Network", "Exception: ${e.message}")
            emptyList() // Return an empty list in case of an exception
        }
    }
}
