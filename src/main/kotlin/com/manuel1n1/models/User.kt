package com.manuel1n1.models

import com.manuel1n1.utils.DateSerializer
import com.manuel1n1.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email:String,
    val password:String,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date)

@Serializable
data class UserLogin(val userName: String, val password: String)

@Serializable
data class UserLoginResponse(val user: UserLogin, val token: String)

val userDb = listOf(UserLogin(
    "intelliJ", "123456"
))