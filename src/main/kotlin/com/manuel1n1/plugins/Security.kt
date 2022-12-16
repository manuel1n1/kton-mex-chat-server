package com.manuel1n1.plugins

import com.manuel1n1.config.JWTConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()
    val validateMS = environment.config.property("jwt.validity_ms").getString()
    val jwtConfig = JWTConfig(secret, issuer, audience, validateMS.toInt())

    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(jwtConfig.verifier)
            validate { credential ->
                if (credential.payload.getClaim("userName").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
