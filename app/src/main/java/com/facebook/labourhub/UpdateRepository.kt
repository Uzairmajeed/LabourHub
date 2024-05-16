package com.facebook.labourhub

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateRepository {

    private val storageRef = FirebaseStorage.getInstance().reference

    suspend fun updateToServer(
        area: String,
        age: String,
        adhaar: String,
        mobile: String,
        category: String
    ): String? {
        return try {
            val gson = Gson()
            val updateData = UpdateData(area, age, adhaar, mobile, category)
            val json = gson.toJson(updateData)

            // Log the JSON data that will be sent to the server
            Log.d("JSON_DATA", json)

            // Create a reference for the JSON file in Firebase Storage
            val jsonRef = storageRef.child("users_data.json")

            // Get the current data from the JSON file (if it exists)
            val currentDataString = withContext(Dispatchers.IO) {
                try {
                    jsonRef.getBytes(MAX_FILE_SIZE.toLong()).await()?.toString(Charsets.UTF_8)
                } catch (e: Exception) {
                    null
                }
            }

            if (currentDataString.isNullOrEmpty()) {
                throw Exception("Current data is empty or null")
            }

            // Parse the current data
            val usersArray = JsonParser.parseString(currentDataString).asJsonArray

            // Find the specific entry and update it
            var entryFound = false
            for (i in 0 until usersArray.size()) {
                val userObject = usersArray[i].asJsonObject
                if (userObject.get("adhaar").asString == adhaar) {
                    userObject.addProperty("area", area)
                    userObject.addProperty("age", age)
                    userObject.addProperty("mobile", mobile)
                    userObject.addProperty("category", category)
                    entryFound = true
                    break
                }
            }

            if (!entryFound) {
                throw Exception("User with given adhaar not found")
            }

            // Convert JSON array to string
            val updatedJsonString = gson.toJson(usersArray)

            // Upload JSON data to Firebase Storage
            jsonRef.putBytes(updatedJsonString.toByteArray()).await()

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


    suspend fun deleteFromServer(adhaar: String): String? {
        return try {
            // Create a reference for the JSON file in Firebase Storage
            val jsonRef = storageRef.child("users_data.json")

            // Get the current data from the JSON file (if it exists)
            val currentDataString = withContext(Dispatchers.IO) {
                try {
                    jsonRef.getBytes(MAX_FILE_SIZE.toLong()).await()?.toString(Charsets.UTF_8)
                } catch (e: Exception) {
                    null
                }
            }

            if (currentDataString.isNullOrEmpty()) {
                throw Exception("Current data is empty or null")
            }

            // Parse the current data
            val usersArray = JsonParser.parseString(currentDataString).asJsonArray

            // Find the specific entry and delete it
            var entryFound = false
            val iterator = usersArray.iterator()
            while (iterator.hasNext()) {
                val userObject = iterator.next().asJsonObject
                if (userObject.get("adhaar").asString == adhaar) {
                    iterator.remove()
                    entryFound = true
                    break
                }
            }

            if (!entryFound) {
                throw Exception("User with given adhaar not found")
            }

            // Convert JSON array to string
            val updatedJsonString = Gson().toJson(usersArray)

            // Upload JSON data to Firebase Storage
            jsonRef.putBytes(updatedJsonString.toByteArray()).await()

            // Log the uploaded data
            Log.d("DATA_UPLOAD", "Data Uploaded Successfully")

            // Return a success message or any identifier as needed
            "Data Deleted Successfully"
        } catch (e: Exception) {
            // Handle upload failure
            e.printStackTrace()
            null
        }
    }

    data class UpdateData(
        val area: String,
        val age: String,
        val adhaar: String,
        val mobile: String,
        val category: String
    )

    companion object {
        private const val MAX_FILE_SIZE = 1024 * 1024 // Example: 1 MB
    }
}
