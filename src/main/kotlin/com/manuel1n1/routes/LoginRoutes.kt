package com.manuel1n1.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.manuel1n1.models.UserLogin
import com.manuel1n1.models.UserLoginResponse
import com.manuel1n1.models.userDb
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.loginRoutes() {
    val secret = environment?.config?.property("jwt.secret")?.getString()
    val issuer = environment?.config?.property("jwt.issuer")?.getString()
    val audience = environment?.config?.property("jwt.audience")?.getString()

    route("/auth") {
        post("/login") {
            val user : UserLogin = call.receive()
            // Check username and password
            if(userDb.contains(user)) {
                val token:String = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("userName", user.userName)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                    .sign(Algorithm.HMAC256(secret))
                call.respond(UserLoginResponse(user, token))
            } else {
                call.respondText("User Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}