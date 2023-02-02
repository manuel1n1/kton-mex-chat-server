package com.manuel1n1.models.response

import com.manuel1n1.db.table.Users
import com.manuel1n1.utils.TimestampSerializer
import com.manuel1n1.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import java.sql.Timestamp
import java.util.*

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email:String,
    var password:String?,
    @Serializable(with = TimestampSerializer::class)
    val createdAt: Timestamp
) {
    companion object {
        fun fromEntity(row: ResultRow, withPassword: Boolean) = if(withPassword)
            User(
                id = row[Users.id],
                email = row[Users.email],
                password = row[Users.password],
                createdAt = Timestamp.from(row[Users.createdAt])
            )
        else
            User(
                id = row[Users.id],
                email = row[Users.email],
                password = "",
                createdAt = Timestamp.from(row[Users.createdAt])
            )
    }
}