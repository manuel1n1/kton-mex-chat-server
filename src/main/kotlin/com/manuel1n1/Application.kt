package com.manuel1n1

import io.ktor.server.application.*
import io.ktor.server.netty.*
import com.manuel1n1.plugins.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress
fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureRouting()
}