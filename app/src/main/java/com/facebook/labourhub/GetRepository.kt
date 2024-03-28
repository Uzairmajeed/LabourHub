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
            val response: HttpResponse = client.get("https://rapi.ifood.tv/recipes.php?searchType=new-qid&keys=33769&appId=4&siteId=1095&auth-token=1212551&version=sv6.0&sort_type=&order=1&deviceModel=&country=US&rowType=new-qid&gridstyle=flat-movie&dltype=1&akp=2411-76947")
            val responseBody = response.receive<String>()
            return responseBody
    }

}
