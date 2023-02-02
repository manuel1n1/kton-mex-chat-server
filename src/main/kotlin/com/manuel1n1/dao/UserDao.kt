package com.manuel1n1.dao

import com.manuel1n1.models.response.User
import java.util.*

interface UserDao {
    suspend fun insert(email: String, password: String?): User
    suspend fun findByID(userId: Int): User?
    suspend fun findByEmail(email: String): User?
    suspend fun isEmailAvailable(email: String): Boolean
    suspend fun updatePassword(userId: UUID, password: String): Boolean
    suspend fun delete(id: UUID): Boolean
}