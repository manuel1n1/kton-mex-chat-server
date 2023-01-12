package com.manuel1n1.routes

import com.manuel1n1.config.JWTConfig
import com.manuel1n1.config.JsonResponse
import com.manuel1n1.dao.UserDao
import com.manuel1n1.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.mindrot.jbcrypt.BCrypt
import java.lang.Exception

fun Route.loginRoutes() {
    val secret = environment?.config?.property("jwt.secret")?.getString()
    val issuer = environment?.config?.property("jwt.issuer")?.getString()
    val audience = environment?.config?.property("jwt.audience")?.getString()
    val validateMS = environment?.config?.property("jwt.validity_ms")?.getString()
    val userDao = UserDao()
    val jwtConfig = JWTConfig(secret!!, issuer!!, audience!!, validateMS!!.toInt())

    route("/auth") {
        post("/login") {
            try {
                val user : LoginRequest = call.receive()
                val userExist = userDao.getUserByEmail(user.userName)
                if(userExist == null)
                    call.respond(status = HttpStatusCode.NotFound,
                        JsonResponse(HttpStatusCode.NotFound, "User not found", "User not found"))
                else if(!BCrypt.checkpw(user.password, userExist.password))
                    call.respond(status = HttpStatusCode.Conflict,
                        JsonResponse(HttpStatusCode.Conflict, "Incorrect Password", "Incorrect Password"))
                else {
                    val token = jwtConfig.sign(user.userName)
                    call.sessions.set(UserSession(userExist.id, token))
                    call.respond(UserLoginResponse(UserData(userExist.id, userExist.email), token))
                }
            } catch (ex: Exception) {
                call.respondText(ex.message!!, status = HttpStatusCode.InternalServerError)
            }
        }
        authenticate("auth-jwt") {
            post("/logout") {
                val infoSession = call.sessions.get<UserSession>()
                println(infoSession)
                call.sessions.clear<UserSession>()
                call.respond(status = HttpStatusCode.OK, "Successful logout")
            }
        }
    }
}