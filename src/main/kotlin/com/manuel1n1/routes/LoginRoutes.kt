package com.manuel1n1.routes

import com.manuel1n1.config.JWTConfig
import com.manuel1n1.models.UserLogin
import com.manuel1n1.dao.UserDao
import com.manuel1n1.models.UserData
import com.manuel1n1.models.UserLoginResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.mindrot.jbcrypt.BCrypt
import java.lang.Exception

fun Route.loginRoutes() {
    val uniqueViolationException = "23505"
    val secret = environment?.config?.property("jwt.secret")?.getString()
    val issuer = environment?.config?.property("jwt.issuer")?.getString()
    val audience = environment?.config?.property("jwt.audience")?.getString()
    val userDao = UserDao()
    val jwtConfig = JWTConfig(secret!!, issuer!!, audience!!)

    route("/auth") {
        post("/login") {
            try {
                val user : UserLogin = call.receive()
                val userExist = userDao.getUserByEmail(user.userName)
                if(userExist == null) {
                    call.respondText("User Not Found", status = HttpStatusCode.NotFound)
                } else if(!BCrypt.checkpw(user.password, userExist.password)) {
                    call.respondText("Incorrect Password", status = HttpStatusCode.Conflict)
                }
                call.respond(UserLoginResponse(UserData(userExist!!.id!!, userExist.email), jwtConfig.sign(user.userName)))
            } catch (ex: Exception) {
                call.respondText(ex.message!!, status = HttpStatusCode.InternalServerError)
            }
        }
        post("/register") {
            try {
                val user : UserLogin = call.receive()
                val newUser = userDao.insert(user)
                call.respond(newUser)
            } catch (ex: Exception) {
                val code = (ex as? ExposedSQLException)
                if(code?.sqlState.equals(uniqueViolationException)) {
                    call.respondText("Email must be unique", status = HttpStatusCode.Conflict)
                } else if (code != null){
                    call.respondText(code.cause!!.message!!, status = HttpStatusCode.InternalServerError)
                } else {
                    call.respondText(ex.message!!, status = HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}