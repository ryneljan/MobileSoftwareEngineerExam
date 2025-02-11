package com.raineru.msee

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.io.IOException

class MockyApiClient(
    private val httpClient: HttpClient
) {

    suspend fun submitForm(mockyId: String): ApiResponse {
        try {
            val response: HttpResponse = httpClient.get(
                "https://run.mocky.io/v3/$mockyId"
            )
            return if (response.status == io.ktor.http.HttpStatusCode.OK) {
                ApiResponse.Success(response.body())
            } else {
                ApiResponse.Error("Error: ${response.status}")
            }
        } catch (e: IOException) {
            return ApiResponse.Error(e.toString())
        }
    }
}
