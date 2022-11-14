package com.manuel1n1.routes

import com.manuel1n1.dao.UserDao
import com.manuel1n1.models.Password
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.userRoutes() {
    val userDao = UserDao()
    route("/user") {
        authenticate("auth-jwt") {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("userName").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms. Hurry up!")
            }
            get("/{id}") {
                try {
                    val id = call.parameters["id"]
                    if(id == null)
                        call.respondText("Id empty", status = HttpStatusCode.BadRequest)
                    else {
                        val user = userDao.getUserById(UUID.fromString(id))
                        if(user == null)
                            call.respondText("User not found", status = HttpStatusCode.NotFound)
                        else
                            user.password = ""
                           call.respond(user!!)
                    }
                } catch (ex: Exception) {
                    call.respondText(ex.message!!, status = HttpStatusCode.InternalServerError)
                }
            }
            put("/{id}") {
                try {
                    val id = call.parameters["id"].toString()
                    val updatePassword : Password = call.receive()
                    if(id.isEmpty())
                        call.respondText("Id empty", status = HttpStatusCode.BadRequest)
                    else {
                        val user = userDao.getUserById(UUID.fromString(id))
                        if(user == null)
                            call.respondText("User not found", status = HttpStatusCode.NotFound)
                        else if(updatePassword.password.isNotEmpty()) {
                            if(userDao.edit(updatePassword, UUID.fromString(id)))
                                call.respondText("User updated")
                            else
                                throw Exception("Something went wrong, try again")
                        } else
                            call.respondText("Password empty", status = HttpStatusCode.BadRequest)
                    }
                } catch (ex: Exception) {
                    call.respondText(ex.message!!, status = HttpStatusCode.InternalServerError)
                }
            }
            delete("/{id}") {
                try {
                    val id = call.parameters["id"]
                    if(id == null)
                        call.respondText("Empty id",status = HttpStatusCode.BadRequest)
                    if(userDao.delete(UUID.fromString(id)))
                        call.respondText("User deleted")
                    else
                        call.respondText("User not found", status = HttpStatusCode.NotFound)
                } catch (ex: Exception) {
                    call.respondText(ex.message!!, status = HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}