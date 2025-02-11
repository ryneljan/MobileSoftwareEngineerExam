package com.raineru.msee

import kotlinx.serialization.Serializable

@Serializable
sealed class ApiResponse {
    @Serializable
    data class Success(val data: FormSubmissionResponse) : ApiResponse()

    @Serializable
    data class Error(val errorMessage: String) : ApiResponse()
}

@Serializable
data class FormSubmissionResponse(
    val status: String,
    val message: String
)
