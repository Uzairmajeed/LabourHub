package com.facebook.labourhub

import android.util.Log
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepository {
    private val client = HttpClient()

    suspend fun postToServer(
        username: String,
        area: String,
        age: String,
        adhaar: String,
        mobile: String,
        imagepath: String?, // Updated parameter to accept image URI
        category: String
    ): String? {
        return try {
            val gson = Gson()
            val postData = PostData(username, area, age, adhaar, mobile, imagepath, category)
            val json = gson.toJson(postData)

            // Log the JSON data that will be sent to the server
            Log.d("JSON_DATA", json)

            val response: HttpResponse = withContext(Dispatchers.IO) {
                client.post("https://minute-eva-na-there.trycloudflare.com/api/users/register") {
                    contentType(ContentType.Application.Json) // Set content type as JSON
                    body = json // Set the JSON data as the request body directly
                }
            }

            val responseBody = response.readText()
            Log.d("RESPONSE_BODY", responseBody) // Log the response body from the server
            Log.d("HTTP_STATUS_CODE", "${response.status.value}")
            responseBody // Return the response body
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    data class PostData(
        val username: String,
        val area: String,
        val age: String,
        val adhaar: String,
        val mobile: String,
        val imagepath: String?, // Updated parameter to accept image path (URI string)
        val category: String
    )
}
