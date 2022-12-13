package com.manuel1n1.dao

import com.manuel1n1.db.DatabaseFactory.dbQuery
import com.manuel1n1.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.mindrot.jbcrypt.BCrypt
import java.sql.Timestamp
import java.time.Instant
import java.util.*

class UserDao {

    suspend fun insert(addUser: RegisterRequest) : User = dbQuery {
        val insertStatement = Users.insert {
            it[id] = UUID.randomUUID()
            it[email] = addUser.email
            it[password] = BCrypt.hashpw(addUser.password, BCrypt.gensalt(10))
            it[createAt] = Instant.now()
        }
        insertStatement.resultedValues?.singleOrNull().let{toUser(it!!, false)}
    }

    suspend fun edit(update: Password, id: UUID) : Boolean = dbQuery {
        Users.update({ Users.id eq id }) {
            it[password] = BCrypt.hashpw(update.password, BCrypt.gensalt(10))
        } > 0
    }

    suspend fun delete(id: UUID): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    suspend fun getAllUsers(): List<User> = dbQuery {
        Users.selectAll().map { toUser(it, false) }
    }

    suspend fun getUserByEmail(email: String): User? = dbQuery {
        Users.select {
            (Users.email eq email)
        }.mapNotNull { toUser(it, true) }
            .singleOrNull()
    }

    suspend fun getUserById(id: UUID) : User? = dbQuery {
        Users.select {
            (Users.id eq id)
        }.mapNotNull { toUser(it, false) }
            .singleOrNull()
    }

    private fun toUser(row: ResultRow, withPassword: Boolean): User =
    if(withPassword)
        User(
            id = row[Users.id],
            email = row[Users.email],
            password = row[Users.password],
            createAt = Timestamp.from(row[Users.createAt])
        )
    else
        User(
            id = row[Users.id],
            email = row[Users.email],
            password = "",
            createAt = Timestamp.from(row[Users.createAt])
        )

    private fun toUser(newUser: RegisterRequest): User =
        User(
            id = UUID.nameUUIDFromBytes(newUser.email.toByteArray()),
            email = newUser.email,
            password = newUser.password,
            createAt = Timestamp.from(Instant.now())
        )
}