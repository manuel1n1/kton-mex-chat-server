package com.manuel1n1.models

import com.manuel1n1.utils.TimestampSerializer
import com.manuel1n1.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.sql.Timestamp
import java.time.Instant

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID?,
    val email:String,
    var password:String,
    @Serializable(with = TimestampSerializer::class)
    val createdAt: Timestamp?)

@Serializable
data class UserLogin(val userName: String, val password: String)

@Serializable
data class UserData(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email: String)

@Serializable
data class UserLoginResponse(val user: UserData, val token: String)

object Users: Table() {
    val id:Column<UUID> = uuid("id")
    val email:Column<String> = varchar("email", 100)
    val password:Column<String> = varchar("password", 100)
    val createdAt:Column<Instant> = timestamp("created_at")
}