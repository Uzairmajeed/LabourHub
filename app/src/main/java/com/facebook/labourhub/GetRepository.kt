package com.facebook.labourhub

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class GetRepository {
    private val client = HttpClient(Android){
       followRedirects = false
        expectSuccess = false
   }

    suspend fun getFromServer(): String? {
            val response: HttpResponse = client.get("https://bed6-2405-201-5503-a87c-edd4-c1da-546f-c046.ngrok-free.app/api/workers/getallworkers")
            val responseBody = response.receive<String>()
            return responseBody
    }

}
