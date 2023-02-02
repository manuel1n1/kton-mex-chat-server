package com.manuel1n1.models.response

import com.manuel1n1.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserData(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email: String)

@Serializable
data class UserLogin(
    val user: UserData,
    val accessToken: String,
    val refreshToken: String
)