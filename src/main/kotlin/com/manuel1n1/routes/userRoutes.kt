package com.manuel1n1.routes

import com.manuel1n1.models.User
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Timestamp
import java.time.Instant
import java.util.*

fun Route.userRoutes() {
    route("/user") {
        authenticate("auth-jwt") {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("userName").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms. Hurry up!")
            }

            get("/hello2") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                println("Hello, $username! Token is expired at $expiresAt ms.")
                call.respond(
                    User(
                        UUID.randomUUID(),
                        "manuel@mail.com",
                        "qwerty",
                        Timestamp.from(Instant.now())
                    )
                )
            }
        }
    }
}