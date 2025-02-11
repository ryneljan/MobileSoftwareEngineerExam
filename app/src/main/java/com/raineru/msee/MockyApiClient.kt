package com.raineru.msee

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.io.IOException

class MockyApiClient(
    private val httpClient: HttpClient
) {

    suspend fun submitForm(mockyId: String): ApiResponse {
        try {
            val response: FormSubmissionResponse = httpClient.get(
                "https://run.mocky.io/v3/$mockyId"
            ).body()
            return ApiResponse.Success(response)
        } catch (e: IOException) {
            return ApiResponse.Error(e.toString())
        }
    }
}
