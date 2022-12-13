package com.manuel1n1.routes

import com.manuel1n1.config.JWTConfig
import com.manuel1n1.config.JsonResponse
import com.manuel1n1.models.LoginRequest
import com.manuel1n1.dao.UserDao
import com.manuel1n1.models.UserData
import com.manuel1n1.models.UserLoginResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt
import java.lang.Exception

fun Route.loginRoutes() {
    val secret = environment?.config?.property("jwt.secret")?.getString()
    val issuer = environment?.config?.property("jwt.issuer")?.getString()
    val audience = environment?.config?.property("jwt.audience")?.getString()
    val userDao = UserDao()
    val jwtConfig = JWTConfig(secret!!, issuer!!, audience!!)

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
                call.respond(UserLoginResponse(UserData(userExist!!.id!!, userExist.email), jwtConfig.sign(user.userName)))
            } catch (ex: Exception) {
                call.respondText(ex.message!!, status = HttpStatusCode.InternalServerError)
            }
        }
    }
}