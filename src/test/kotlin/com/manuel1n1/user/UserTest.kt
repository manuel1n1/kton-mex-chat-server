package com.manuel1n1.user

import com.manuel1n1.models.*
import com.manuel1n1.models.request.LoginRequest
import com.manuel1n1.models.request.SignUpRequest
import com.manuel1n1.models.response.User
import com.manuel1n1.models.response.UserLogin
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
    private val usersRoute = "users"
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
        val createdUser = client.post("$baseRoute/$usersRoute") {
            contentType(ContentType.Application.Json)
            setBody(SignUpRequest( "intelliJ", "123456", "123456"))
        }
        assertEquals(HttpStatusCode.Created, createdUser.status)
        //login
        val loginUser = client.post("$baseRoute/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest( "intelliJ", "123456"))
        }
        assertEquals(HttpStatusCode.OK, loginUser.status)
        //check user hello
        val login = loginUser.body<UserLogin>()
        assertEquals(createdUser.body<User>().id, login.user!!.id, "Not same id")
        client.get("$baseRoute/users/hello"){
            contentType(ContentType.Application.Json)
            headers["Authorization"] = "Bearer ${login.accessToken}"
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            //assertContains("Hello, intelliJ! Token is expired at  ms. Hurry up!", bodyAsText())
        }
        //check user data
        client.get("$baseRoute/$usersRoute/${login.user!!.id}"){
            contentType(ContentType.Application.Json)
            headers["Authorization"] = "Bearer ${login.accessToken}"
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            //assertEquals(User, body())
        }
        //update password
        /*client.put("$baseRoute/$usersRoute/${login.user!!.id}"){
            contentType(ContentType.Application.Json)
            headers["Authorization"] = "Bearer ${login.accessToken}"
            setBody(Password("123456"))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("User updated", bodyAsText())
        }
        //delete user
        client.delete("$baseRoute/$usersRoute/${login.user.id}"){
            headers["Authorization"] = "Bearer ${login.token}"
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("User deleted", bodyAsText())
        }*/
    }
}