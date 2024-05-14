package com.facebook.labourhub
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class GetRepository {
    private val storageRef = FirebaseStorage.getInstance().reference
    private val defaultFileName = "users_data.json" // Default file name

    suspend fun getFromFirebaseStorage(fileName: String = defaultFileName): String? {
        return try {
            // Create a reference for the JSON file in Firebase Storage
            val jsonRef = storageRef.child(fileName)

            // Download the JSON file
            val jsonFile = jsonRef.getBytes(MAX_FILE_SIZE.toLong()).await()?.toString(Charsets.UTF_8)

            jsonFile
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("GetRepository", "Exception: ${e.message}")
            null
        }
    }

    companion object {
        private const val MAX_FILE_SIZE = 1024 * 1024 // Example: 1 MB
    }
}
