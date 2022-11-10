package com.manuel1n1.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JWTConfig(secret: String, private val issuer: String, private val audience: String) {
    private val validityInMs = 36_000_00 * 1
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier =
        JWT.require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
            .build()

    fun sign(name: String): String = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("userName", name)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}