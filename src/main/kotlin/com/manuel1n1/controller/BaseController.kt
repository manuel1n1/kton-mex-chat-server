package com.manuel1n1.controller

import com.manuel1n1.dao.UserDao
import com.manuel1n1.exception.BadRequestException
import com.manuel1n1.exception.UnauthorizedActivityException
import com.manuel1n1.models.request.LoginRequest
import com.manuel1n1.models.request.SignUpRequest
import com.manuel1n1.models.request.UpdatePasswordRequest
import com.manuel1n1.models.response.User
import com.manuel1n1.utils.PasswordEncryptor
import com.manuel1n1.utils.isAlphaNumeric
import com.manuel1n1.utils.isEmailValid
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class BaseController : KoinComponent {
    private val userDao by inject<UserDao>()
    private val passwordEncryptor by inject<PasswordEncryptor>()

    internal fun validateLogInFieldsOrThrowException(
        loginRequest: LoginRequest
    ) {
        val message = when {
            (loginRequest.userName.isBlank() or (loginRequest.password.isBlank())) -> "Credentials fields should not be blank"
            (!loginRequest.userName.isEmailValid()) -> "Email invalid"
            (loginRequest.password.length !in (8..50)) -> "Password should be of min 8 and max 50 character in length"
            else -> return
        }
        throw BadRequestException(message)
    }

    internal fun validateRegisterFieldsOrThrowException(
        signUpRequest: SignUpRequest
    ) {
        val message = when {
            (signUpRequest.email.isBlank() or (signUpRequest.email.isBlank()) or (signUpRequest.password.isBlank()) or (signUpRequest.confirmPassword.isBlank())) -> "Fields should not be blank"
            (!signUpRequest.email.isEmailValid()) -> "Email invalid"
            (!signUpRequest.email.isAlphaNumeric()) -> "No special characters allowed in username"
            (signUpRequest.email.length !in (4..30)) -> "Username should be of min 4 and max 30 character in length"
            (signUpRequest.password.length !in (8..50)) -> "Password should be of min 8 and max 50 character in length"
            (signUpRequest.confirmPassword.length !in (8..50)) -> "Password should be of min 8 and max 50 character in length"
            (signUpRequest.password != signUpRequest.confirmPassword) -> "Passwords do not match"
            else -> return
        }

        throw BadRequestException(message)
    }

    internal fun validateUpdatePasswordFieldsOrThrowException(
        updatePasswordRequest: UpdatePasswordRequest
    ) {
        val message = when {
            (updatePasswordRequest.currentPassword.isBlank() || updatePasswordRequest.newPassword.isBlank()
                    || updatePasswordRequest.confirmNewPassword.isBlank()) -> {
                "Password field should not be blank"
            }
            updatePasswordRequest.newPassword != updatePasswordRequest.confirmNewPassword -> "Passwords do not match"
            updatePasswordRequest.newPassword.length !in (8..50) -> "Password should be of min 8 and max 50 character in length"
            updatePasswordRequest.confirmNewPassword.length !in (8..50) -> "Password should be of min 8 and max 50 character in length"
            else -> return
        }

        throw BadRequestException(message)
    }

    internal fun verifyPasswordOrThrowException(password: String, user: User) {
        user.password?.let {
            if (!passwordEncryptor.validatePassword(password, it))
                throw UnauthorizedActivityException("Authentication failed: Invalid credentials")
        } ?: throw UnauthorizedActivityException("Authentication failed: Invalid credentials")
    }

    internal suspend fun verifyEmail(email: String) {
        if (!userDao.isEmailAvailable(email)) {
            throw BadRequestException("Authentication failed: Email is already taken")
        }
    }

    internal fun getEncryptedPassword(password: String): String {
        return passwordEncryptor.encryptPassword(password)
    }


}