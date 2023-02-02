package com.manuel1n1.models.response

import com.manuel1n1.db.table.Users
import com.manuel1n1.utils.TimestampSerializer
import com.manuel1n1.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import java.sql.Timestamp
import java.util.*

@Serializable
data class UserSignUp(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email:String,
    @Serializable(with = TimestampSerializer::class)
    val createdAt: Timestamp
) {
    companion object {
        fun fromEntity(row: ResultRow) = UserSignUp(
            id = row[Users.id],
            email = row[Users.email],
            createdAt = Timestamp.from(row[Users.createdAt])
        )
    }
}