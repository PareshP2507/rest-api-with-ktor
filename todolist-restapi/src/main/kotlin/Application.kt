package com.psquare

import com.fasterxml.jackson.databind.SerializationFeature
import com.psquare.com.psquare.todolist.userApi
import com.psquare.todolist.auth.JwtConfig
import com.psquare.todolist.dao.Todos
import com.psquare.todolist.dao.Users
import com.psquare.todolist.models.PayloadError
import com.psquare.todolist.repositories.TodoRepositoryImpl
import com.psquare.todolist.repositories.UserRepositoryImpl
import com.psquare.todolist.services.TodoServiceImpl
import com.psquare.todolist.services.UserServiceImpl
import com.psquare.todolist.todoApi
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

private val todoServices =  TodoServiceImpl(TodoRepositoryImpl())
private val userServices = UserServiceImpl(UserRepositoryImpl())

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    initDB()

    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = "ktor.io"
            validate {
                it.payload.getClaim("id").asInt()?.let { id -> userServices.findUserById(id) }
            }
        }
    }

    install(Routing) {
        todoApi(userServices, todoServices)
        userApi(userServices)
    }

    install(StatusPages) {

        // Handle Unauthorised globally
        status(HttpStatusCode.Unauthorized) {
            val payloadError = PayloadError(
                HttpStatusCode.Unauthorized.value,
                "You are unauthorised to perform this action"
            )
            call.respond(HttpStatusCode.Unauthorized, payloadError)
        }

        this.exception<Throwable> { e ->
            val payloadError = PayloadError(HttpStatusCode.InternalServerError.value, e.localizedMessage)
            call.respond(HttpStatusCode.InternalServerError, payloadError)
        }
    }

    install(CallLogging)

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
}

fun initDB() {
    Database.connect("jdbc:postgresql://localhost:5432/todo-db", driver = "org.postgresql.Driver",
        user = "testmac", password = "")
    createTables()
    LoggerFactory.getLogger(Application::class.java.simpleName).info("DB initialized")
}

private fun createTables() = transaction {
    SchemaUtils.create(Todos)
    SchemaUtils.create(Users)
}

