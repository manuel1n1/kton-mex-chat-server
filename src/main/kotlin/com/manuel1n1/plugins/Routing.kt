package com.manuel1n1.plugins

import com.manuel1n1.routes.loginRoutes
import com.manuel1n1.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        route("/api/v1") {
            loginRoutes()
            userRoutes()
            get("/") {
                call.respondText("Hello World!")
            }
        }
    }
}
