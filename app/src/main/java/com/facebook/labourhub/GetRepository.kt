package com.facebook.labourhub

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
            val response: HttpResponse = client.get("https://amended-cds-murder-readings.trycloudflare.com/api/users/employees")
            val responseBody = response.receive<String>()
            return responseBody
    }

}
