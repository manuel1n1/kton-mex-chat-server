package com.manuel1n1

import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.manuel1n1.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*


class ApplicationTest {
    private val baseRoute = "/api/v1"
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("$baseRoute/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}