package com.manuel1n1.routes

import com.manuel1n1.config.JsonResponse
import com.manuel1n1.dao.UserDao
import com.manuel1n1.models.request.SignUpRequest
import com.manuel1n1.models.request.UpdatePasswordRequest
import com.manuel1n1.models.sessions.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.koin.ktor.ext.inject
import org.mindrot.jbcrypt.BCrypt
import java.util.*

fun Route.userRoutes() {
    val uniqueViolationException = "23505"
    val userDao by inject<UserDao>()
    route("/users") {
        post {
            try {
                val user : SignUpRequest = call.receive()
                val password = BCrypt.hashpw(user.password, BCrypt.gensalt(10))
                val newUser = userDao.insert(user.email, password)
                call.respond(status = HttpStatusCode.Created, newUser)
            } catch (ex: java.lang.Exception) {
                val code = (ex as? ExposedSQLException)
                if(code?.sqlState.equals(uniqueViolationException))
                    call.respond(status = HttpStatusCode.Conflict,
                        JsonResponse(HttpStatusCode.Conflict, ex.cause?.message.toString(), "Email must be unique"))
                else if (code != null)
                    call.respond(status = HttpStatusCode.InternalServerError,
                        JsonResponse(HttpStatusCode.InternalServerError, code.cause!!.message!!, "Internal Server Error"))
                else
                    call.respond(status = HttpStatusCode.InternalServerError,
                        JsonResponse(HttpStatusCode.InternalServerError, ex.message!!, "Internal Server Error"))
            }
        }
        authenticate("auth-jwt") {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("userName").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms. Hurry up!")
            }
            get("/check-session") {
                val userSession = call.sessions.get<UserSession>()
                if(userSession != null)
                    call.respondText("Session ID is ${userSession.id}.\nToken is ${userSession.token}")
                else
                    call.respondText("Session doesn't exist or is expired")
            }
            get("/{id}") {
                try {
                    val id = call.parameters["id"]
                    if(id == null)
                        call.respond(status = HttpStatusCode.BadRequest,
                            JsonResponse(HttpStatusCode.BadRequest, "Id empty", "Id empty"))
                    else {
                        val user = userDao.findByID(UUID.fromString(id))
                        if(user == null)
                            call.respond(status = HttpStatusCode.NotFound,
                                JsonResponse(HttpStatusCode.NotFound, "User not found", "User not found"))
                        else
                            user.password = ""
                           call.respond(user!!)
                    }
                } catch (ex: Exception) {
                    call.respond(status = HttpStatusCode.InternalServerError,
                        JsonResponse(HttpStatusCode.InternalServerError, ex.message!!, "Internal Server Error"))
                }
            }
            put("/{id}") {
                try {
                    val id = call.parameters["id"].toString()
                    val updatePassword : UpdatePasswordRequest = call.receive()
                    if(id.isEmpty())
                        call.respond(status = HttpStatusCode.BadRequest,
                            JsonResponse(HttpStatusCode.BadRequest, "Id empty", "Id empty"))
                    else {
                        val user = userDao.findByID(UUID.fromString(id))
                        if(user == null)
                            call.respond(status = HttpStatusCode.NotFound,
                                JsonResponse(HttpStatusCode.NotFound, "User not found", "User not found"))
                        else if(updatePassword.newPassword.isNotEmpty()) {
                            val password = BCrypt.hashpw(updatePassword.newPassword, BCrypt.gensalt(10))
                            if(userDao.updatePassword(UUID.fromString(id), password))
                                call.respondText("User updated")
                            else
                                throw Exception("Something went wrong, try again")
                        } else
                            call.respond(status = HttpStatusCode.NotFound,
                                JsonResponse(HttpStatusCode.NotFound, "Empty Password", "Empty Password"))
                    }
                } catch (ex: Exception) {
                    call.respond(status = HttpStatusCode.InternalServerError,
                        JsonResponse(HttpStatusCode.InternalServerError, ex.message!!, "Internal Server Error"))
                }
            }
            delete("/{id}") {
                try {
                    val id = call.parameters["id"]
                    if(id == null)
                        call.respond(status = HttpStatusCode.BadRequest,
                            JsonResponse(HttpStatusCode.BadRequest, "Id empty", "Id empty"))
                    if(userDao.delete(UUID.fromString(id)))
                        call.respondText("User deleted")
                    else
                        call.respond(status = HttpStatusCode.NotFound,
                            JsonResponse(HttpStatusCode.NotFound, "User not found", "User not found"))
                } catch (ex: Exception) {
                    call.respond(status = HttpStatusCode.InternalServerError,
                        JsonResponse(HttpStatusCode.InternalServerError, ex.message!!, "Internal Server Error"))
                }
            }
        }
    }
}