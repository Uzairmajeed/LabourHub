package com.facebook.labourhub

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class Network {
    private val getRepository = GetRepository()

    suspend fun fetchData(): List<Post?> {
        return try {
            val responseBody = getRepository.getFromServer()
            Log.e("Network", responseBody.toString())
            val jsonParser = JsonParser()
            val jsonObject = jsonParser.parse(responseBody) as JsonObject
            val resultsArray = jsonObject.getAsJsonArray("user")
            val posts = mutableListOf<Post?>()

            for (i in 0 until resultsArray.size()) {
                val result = resultsArray.get(i).asJsonObject
                val post = try {
                    Gson().fromJson(result, Post::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("Network", "Exception: ${e.message}")
                    null
                }
                post?.let { posts.add(it) } // Add non-null posts to the list
            }
            posts
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Network", "Exception: ${e.message}")
            emptyList() // Return an empty list in case of an exception
        }
    }

}

