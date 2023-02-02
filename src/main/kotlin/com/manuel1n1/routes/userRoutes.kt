package com.manuel1n1.routes

import com.manuel1n1.controller.auth.AuthController
import com.manuel1n1.models.request.LoginRequest
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.userRoutes() {

    route("/users") {
        post {
            /*val loginRequest : LoginRequest = call.receive()
            val loginResponse = authController.login(loginRequest)
            val response = generateHttpResponse(loginResponse)
            call.respond(response.code, response.body)*/
        }
        authenticate("auth-jwt") {
            get("/hello") {
                /*val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("userName").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms. Hurry up!")*/
            }
            get("/check-session") {
                /*val userSession = call.sessions.get<UserSession>()
                if(userSession != null)
                    call.respondText("Session ID is ${userSession.id}.\nToken is ${userSession.token}")
                else
                    call.respondText("Session doesn't exist or is expired")*/
            }
            get("/{id}") {
                /*try {
                    val id = call.parameters["id"]
                    if(id == null)
                        call.respond(status = HttpStatusCode.BadRequest,
                            JsonResponse(HttpStatusCode.BadRequest, "Id empty", "Id empty"))
                    else {
                        val user = userDao.getUserById(UUID.fromString(id))
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
                }*/
            }
            put("/{id}") {
                /*try {
                    val id = call.parameters["id"].toString()
                    val updatePassword : UpdatePasswordRequest = call.receive()
                    if(id.isEmpty())
                        call.respond(status = HttpStatusCode.BadRequest,
                            JsonResponse(HttpStatusCode.BadRequest, "Id empty", "Id empty"))
                    else {
                        val user = userDao.getUserById(UUID.fromString(id))
                        if(user == null)
                            call.respond(status = HttpStatusCode.NotFound,
                                JsonResponse(HttpStatusCode.NotFound, "User not found", "User not found"))
                        else if(updatePassword.password.isNotEmpty()) {
                            if(userDao.edit(updatePassword, UUID.fromString(id)))
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
                }*/
            }
            delete("/{id}") {
                /*try {
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
                }*/
            }
        }
    }
}