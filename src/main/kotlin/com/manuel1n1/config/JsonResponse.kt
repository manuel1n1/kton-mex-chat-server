package com.manuel1n1.config

import io.ktor.http.*
import kotlinx.serialization.Serializable

/*message string variable will keep for short description*/
@Serializable
data class JsonResponse(
    val statusCode: Int,
    val statusCodeDescription: String,
    val error: String,
    val message: String
) {
    constructor(statusCode: HttpStatusCode, error: String, message: String) : this(
        statusCode = statusCode.value,
        statusCodeDescription = statusCode.description,
        error = error,
        message = message
    )
}