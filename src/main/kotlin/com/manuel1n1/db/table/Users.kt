package com.manuel1n1.db.table

import com.manuel1n1.dao.UserDao
import com.manuel1n1.db.DatabaseFactory
import com.manuel1n1.db.table.Users.id
import com.manuel1n1.models.response.User
import com.manuel1n1.models.response.UserSignUp
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.*

object Users: Table() {
    val id: Column<UUID> = uuid("id")
    val email:Column<String> = varchar("email", 100)
    val password:Column<String> = varchar("password", 100)
    val createdAt:Column<Instant> = timestamp("created_at")
}

object UserRepository : UserDao {

    override suspend fun insert(email: String, password: String?): UserSignUp {
        return DatabaseFactory.dbQuery {
            val insertStatement = Users.insert {
                it[id] = UUID.randomUUID()
                it[Users.email] = email
                it[Users.password] = password!!
                it[createdAt] = Instant.now()
            }
            insertStatement.resultedValues?.singleOrNull().let {
                UserSignUp.fromEntity(it!!)
            }
        }
    }

    override suspend fun findByID(userId: UUID): User? {
        return DatabaseFactory.dbQuery {
            Users.select {
                (id eq id)
            }.mapNotNull { User.fromEntity(it, false) }
                .singleOrNull()
        }
    }

    override suspend fun findByEmail(email: String): User? {
        return DatabaseFactory.dbQuery { Users.select {
            (Users.email eq email)
        }.mapNotNull { User.fromEntity(it) }
            .singleOrNull() }
    }

    override suspend fun isEmailAvailable(email: String): Boolean {
        return DatabaseFactory.dbQuery {
            Users.select {
                (Users.email eq email)
            }.firstOrNull() == null
        }
    }

    override suspend fun updatePassword(userId: UUID, password: String): Boolean {
        return DatabaseFactory.dbQuery {
            Users.update({ id eq id }) {
                it[Users.password] = password
            } > 0
        }
    }

    override suspend fun delete(id: UUID): Boolean {
        return DatabaseFactory.dbQuery {
            Users.deleteWhere { Users.id eq id } > 0
        }
    }

}