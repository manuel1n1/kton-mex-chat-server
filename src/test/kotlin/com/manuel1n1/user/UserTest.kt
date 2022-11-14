package com.manuel1n1.user

import com.manuel1n1.models.Password
import com.manuel1n1.models.User
import com.manuel1n1.models.UserLogin
import com.manuel1n1.models.UserLoginResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
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
        //TODO Improve unit test with json validations
        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
        //user register
        val createdUser = client.post("$baseRoute/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(UserLogin( "intelliJ", "123456"))
        }
        assertEquals(HttpStatusCode.OK, createdUser.status)
        //login
        val loginUser = client.post("$baseRoute/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(UserLogin( "intelliJ", "123456"))
        }
        assertEquals(HttpStatusCode.OK, loginUser.status)
        //check user hello
        val login = loginUser.body<UserLoginResponse>()
        assertEquals(createdUser.body<User>().id, login.user.id, "Not same id")
        client.get("$baseRoute/user/hello"){
            contentType(ContentType.Application.Json)
            headers["Authorization"] = "Bearer ${login.token}"
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            //assertContains("Hello, intelliJ! Token is expired at  ms. Hurry up!", bodyAsText())
        }
        //check user data
        client.get("$baseRoute/user/${login.user.id}"){
            contentType(ContentType.Application.Json)
            headers["Authorization"] = "Bearer ${login.token}"
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            //assertEquals(User, body())
        }
        //update password
        client.put("$baseRoute/user/${login.user.id}"){
            contentType(ContentType.Application.Json)
            headers["Authorization"] = "Bearer ${login.token}"
            setBody(Password("123456"))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("User updated", bodyAsText())
        }
        //delete user
        client.delete("$baseRoute/user/${login.user.id}"){
            headers["Authorization"] = "Bearer ${login.token}"
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("User deleted", bodyAsText())
        }
    }
}