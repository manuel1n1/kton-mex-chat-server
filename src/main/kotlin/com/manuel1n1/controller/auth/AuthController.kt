package com.manuel1n1.controller.auth

import com.manuel1n1.config.GeneralResponse
import com.manuel1n1.controller.BaseController
import com.manuel1n1.dao.UserDao
import com.manuel1n1.exception.BadRequestException
import com.manuel1n1.exception.UnauthorizedActivityException
import com.manuel1n1.models.request.LoginRequest
import com.manuel1n1.models.request.SignUpRequest
import com.manuel1n1.models.request.UpdatePasswordRequest
import com.manuel1n1.models.response.*
import io.ktor.http.*
import io.ktor.server.application.*
import org.koin.core.component.inject

class DefaultAuthController : BaseController(), AuthController {

    private val userDao by inject<UserDao>()
    private val tokenProvider by inject<TokenProvider>()

    override suspend fun login(loginRequest: LoginRequest) {
        try {
            validateLogInFieldsOrThrowException(loginRequest)
            userDao.findByEmail(loginRequest.userName)?.let {
                verifyPasswordOrThrowException(loginRequest.password, it)
                val tokens = tokenProvider.createTokens(it)
                UserLogin(
                    UserData(it.id, it.email),
                    tokens.access_token,
                    tokens.refresh_token
                )
            }.takeIf {
                throw UnauthorizedActivityException("Authentication failed: Invalid credentials")
            }
        } catch (e: BadRequestException) {
            GeneralResponse(HttpStatusCode.BadRequest, e.message,e.message)
        } catch (e: UnauthorizedActivityException) {
            GeneralResponse(HttpStatusCode.Unauthorized, e.message, e.message)
        }
    }

    override suspend fun signUp(signUpRequest: SignUpRequest) {
        try {
            validateRegisterFieldsOrThrowException(signUpRequest)
            verifyEmail(signUpRequest.email)
            val encryptedPassword = getEncryptedPassword(signUpRequest.password)
            userDao.insert(signUpRequest.email, encryptedPassword).let {
                UserSignUp.fromEntity(it)
            }
        } catch (e: BadRequestException) {
            GeneralResponse(HttpStatusCode.BadRequest,e.message,e.message)
        } catch (e: UnauthorizedActivityException) {
            GeneralResponse(HttpStatusCode.Unauthorized, e.message, e.message)
        }
    }

    override suspend fun updateAccountPassword(
        updatePasswordRequest: UpdatePasswordRequest,
        ctx: ApplicationCall
    ) {
        TODO("Not yet implemented")
    }
}

interface AuthController {

    suspend fun login(loginRequest: LoginRequest)

    suspend fun signUp(signUpRequest: SignUpRequest)

    suspend fun updateAccountPassword(
        updatePasswordRequest: UpdatePasswordRequest,
        ctx: ApplicationCall
    )
}