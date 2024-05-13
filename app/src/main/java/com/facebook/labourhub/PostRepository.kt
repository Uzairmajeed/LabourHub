package com.facebook.labourhub

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import org.json.JSONObject

class PostRepository {

    private val storageRef = FirebaseStorage.getInstance().reference

    suspend fun postToServer(userData: JSONObject): String? {
        return try {
            // Create a reference for the JSON file in Firebase Storage
            val jsonRef = storageRef.child("users_data.json")

            // Get the current data from the JSON file (if it exists)
            val currentData: JSONArray? = try {
                val currentDataString = jsonRef.getBytes(MAX_FILE_SIZE.toLong()).await()?.toString(Charsets.UTF_8)
                if (!currentDataString.isNullOrEmpty()) JSONArray(currentDataString) else null
            } catch (e: Exception) {
                null
            }

            // Append the new user data to the JSON array if it exists, or create a new array
            val usersArray = currentData ?: JSONArray()
            usersArray.put(userData)
            Log.d("JsonData", userData.toString())


            // Convert JSON array to string
            val jsonString = usersArray.toString()

            // Upload JSON data to Firebase Storage
            jsonRef.putBytes(jsonString.toByteArray())

            // Log the uploaded data
            Log.d("DATA_UPLOAD", "Data Uploaded Successfully")

            // Return a success message or any identifier as needed
            "Data Uploaded Successfully"
        } catch (e: Exception) {
            // Handle upload failure
            e.printStackTrace()
            null
        }
    }
    companion object {
        private const val MAX_FILE_SIZE = 1024 * 1024 // Example: 1 MB
    }

}

