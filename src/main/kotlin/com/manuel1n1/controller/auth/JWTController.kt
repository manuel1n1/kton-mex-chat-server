package com.manuel1n1.controller.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.manuel1n1.models.response.TokenResponse
import com.manuel1n1.models.response.User
import java.util.*

class JWTController(private val secret: String,private val issuer: String,
                    private val audience: String, private val validityInMs: Long): TokenProvider {
    private val refreshValidityInMs: Long = 3600000L * 24L * 30L // 30 days
    private val algorithm: Algorithm = Algorithm.HMAC256(secret)

    private val verifier: JWTVerifier =
        JWT.require(algorithm)
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

    override fun createTokens(user: User) = TokenResponse(
        createAccessToken(user, getTokenExpiration()),
        createRefreshToken(user, getTokenExpiration(refreshValidityInMs))
    )

    override fun verifyTokenType(token: String): String {
        return verifier.verify(token).claims["tokenType"]!!.asString()
    }

    override fun verifyToken(token: String): Int? {
        return verifier.verify(token).claims["userName"]?.asInt()
    }

    override fun getTokenExpiration(token: String): Date {
        return verifier.verify(token).expiresAt
    }

    private fun createAccessToken(user: User, expiration: Date) = JWT.create()
        .withSubject("Authentication")
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("userName", user.email)
        .withClaim("tokenType", "accessToken")
        .withExpiresAt(expiration)
        .sign(algorithm)

    private fun createRefreshToken(user: User, expiration: Date) = JWT.create()
        .withSubject("Authentication")
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("userName", user.email)
        .withClaim("tokenType", "refreshToken")
        .withExpiresAt(expiration)
        .sign(algorithm)

    private fun getTokenExpiration(validity: Long = validityInMs) = Date(System.currentTimeMillis() + validity)
}

interface TokenProvider {
    fun createTokens(user: User): TokenResponse
    fun verifyTokenType(token: String): String
    fun verifyToken(token: String): Int?
    fun getTokenExpiration(token: String): Date
}