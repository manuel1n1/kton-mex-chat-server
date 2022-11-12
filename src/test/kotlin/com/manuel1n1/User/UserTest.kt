package com.manuel1n1.User

import com.manuel1n1.models.UserLogin
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class UserTest {
    private val baseRoute = "/api/v1"
    @Test
    fun testRegisterUser() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
        val createdUser = client.post("$baseRoute/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(UserLogin( "intelliJ", "123456"))
        }
        assertEquals(HttpStatusCode.OK, createdUser.status)
        val loginUser = client.post("$baseRoute/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(UserLogin( "intelliJ", "123456"))
        }
        assertEquals(HttpStatusCode.OK, loginUser.status)
    }
}