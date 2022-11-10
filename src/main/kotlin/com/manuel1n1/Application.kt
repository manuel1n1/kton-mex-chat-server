package com.manuel1n1

import com.manuel1n1.plugins.configureRouting
import com.manuel1n1.plugins.configureSecurity
import com.manuel1n1.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureRouting()
}