package com.facebook.labourhub

import android.util.Log
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateRepository {

    private val client = HttpClient()

    suspend fun updateToServer(
        area: String,
        age: String,
        adhaar: String,
        mobile: String,
        category: String
    ): String? {
        return try {
            val gson = Gson()
            val updateData = UpdateData( area, age, adhaar, mobile, category)
            val json = gson.toJson(updateData)

            // Log the JSON data that will be sent to the server
            Log.d("JSON_DATA", json)

            val response: HttpResponse = withContext(Dispatchers.IO) {
                client.put("https://bed6-2405-201-5503-a87c-edd4-c1da-546f-c046.ngrok-free.app/api/workers/update") {
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

    data class UpdateData(
        val area: String,
        val age: String,
        val adhaar: String,
        val mobile: String,
        val category: String
    )
}