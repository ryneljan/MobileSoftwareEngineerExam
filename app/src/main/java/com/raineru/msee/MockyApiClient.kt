package com.raineru.msee

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.io.IOException

class MockyApiClient(
    private val httpClient: HttpClient
) {

    suspend fun submitForm(mockyId: String): String {
        try {
            val response: HttpResponse = httpClient.get(
                "https://run.mocky.io/v3/$mockyId"
            )
            return response.bodyAsText()
        } catch (e: IOException) {
            return e.toString()
        }
    }
}
