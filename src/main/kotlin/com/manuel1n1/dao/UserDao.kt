package com.manuel1n1.dao

import com.manuel1n1.models.Users
import com.manuel1n1.db.DatabaseFactory.dbQuery
import com.manuel1n1.models.User
import com.manuel1n1.models.UserLogin
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.mindrot.jbcrypt.BCrypt
import java.sql.Timestamp
import java.time.Instant
import java.util.*

class UserDao {

    suspend fun getAllUsers(): List<User> = dbQuery {
        Users.selectAll().map { toUser(it) }
    }

    suspend fun getUserByEmail(email: String): User? = dbQuery {
        Users.select {
            (Users.email eq email)
        }.mapNotNull { toUser(it) }
            .singleOrNull()
    }

    private fun toUser(row: ResultRow): User =
        User(
            id = row[Users.id],
            email = row[Users.email],
            password = row[Users.password],
            createdAt = Timestamp.from(row[Users.createdAt])
        )

    private fun toUser(newUser: UserLogin): User =
        User(
            id = UUID.nameUUIDFromBytes(newUser.userName.toByteArray()),
            email = newUser.userName,
            password = newUser.password,
            createdAt = Timestamp.from(Instant.now())
        )

    suspend fun insert(addUser: UserLogin) : User = dbQuery {
        val insertStatement = Users.insert {
            it[id] = UUID.randomUUID()
            it[email] = addUser.userName
            it[password] = BCrypt.hashpw(addUser.password, BCrypt.gensalt(10))
            it[createdAt] = Instant.now()
        }
        insertStatement.resultedValues?.singleOrNull().let{toUser(it!!)}
    }
}