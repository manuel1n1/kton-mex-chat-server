package com.manuel1n1.utils

import org.mindrot.jbcrypt.BCrypt

class PasswordEncryptor : PasswordEncryptorContract {

    override fun validatePassword(attempt: String, userPassword: String): Boolean {
        return BCrypt.checkpw(attempt, userPassword)
    }

    override fun encryptPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}

interface PasswordEncryptorContract {
    fun validatePassword(attempt: String, userPassword: String): Boolean
    fun encryptPassword(password: String): String
}