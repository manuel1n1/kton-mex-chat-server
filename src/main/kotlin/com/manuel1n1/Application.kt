package com.manuel1n1

import com.manuel1n1.db.DatabaseFactoryInterface
import com.manuel1n1.plugins.configureRouting
import com.manuel1n1.plugins.configureSecurity
import com.manuel1n1.plugins.configureSerialization
import com.manuel1n1.plugins.koinModules
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.koin
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {

    val databaseProvider by inject<DatabaseFactoryInterface>()

    configureSecurity()
    configureSerialization()
    configureRouting()
    koin {
        modules(listOf(koinModules))
    }

    databaseProvider.init()
}