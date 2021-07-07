package com.psquare.todolist

import com.psquare.com.psquare.todolist.models.PayloadSuccess
import com.psquare.todolist.dao.TodoItem
import com.psquare.todolist.dao.User
import com.psquare.todolist.models.PayloadError
import com.psquare.todolist.services.TodoService
import com.psquare.todolist.services.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.todoApi(userService: UserService, todoService: TodoService) {
    route("/api") {
        authenticate {
            get("/todos/") {
                val user = call.authentication.principal<User>()
                user?.let {
                    userService.retrieveUserEntityById(it.id)?.let { userEntity ->
                        // Retrieve all the records
                        val todos = todoService.getAll(userEntity)
                        val payloadSuccess = PayloadSuccess(HttpStatusCode.OK.value, "Record retrieved successfully", todos)
                        call.respond(HttpStatusCode.OK, payloadSuccess)
                    } ?: run {
                        val error = PayloadError(HttpStatusCode.BadRequest.value, "Not such user exist")
                        call.respond(HttpStatusCode.BadRequest, error)
                    }
                } ?: run {
                    val error = PayloadError(HttpStatusCode.BadRequest.value, "Not such user exist")
                    call.respond(HttpStatusCode.BadRequest, error)
                }
            }

            get("/todos/{id}/") {
                val id = call.parameters["id"] ?: run {
                    val error = PayloadError(HttpStatusCode.InternalServerError.value, "Request is missing id")
                    call.respond(HttpStatusCode.InternalServerError, error)
                    return@get
                }
                try {
                    val todo = todoService.getTodo(id.toInt())
                    call.respond(todo ?: HttpStatusCode.NotFound)
                } catch (e: Throwable) {
                    val payloadError: PayloadError = if (e is NumberFormatException) {
                        PayloadError(HttpStatusCode.InternalServerError.value, "Param 'id' is not in expected format")
                    } else {
                        PayloadError.buildErrorFor500()
                    }
                    call.respond(HttpStatusCode.InternalServerError, payloadError)
                }
            }

            post("/todo") {
                val todo = call.receive<TodoItem>()

                val user = call.authentication.principal<User>()
                user?.let {
                    userService.retrieveUserEntityById(it.id)?.let { userEntity ->
                        val todoItem = todoService.create(userEntity, todo)
                        call.respond(HttpStatusCode.Created, todoItem)
                    } ?: run {
                        val error = PayloadError(HttpStatusCode.BadRequest.value, "Not such user exist")
                        call.respond(HttpStatusCode.BadRequest, error)
                    }
                } ?: run {
                    val error = PayloadError(HttpStatusCode.BadRequest.value, "Not such user exist")
                    call.respond(HttpStatusCode.BadRequest, error)
                }
            }

            put("todos/{id}") {
                // Look for id param
                val id = call.parameters["id"] ?: run {
                    val error = PayloadError(HttpStatusCode.InternalServerError.value, "Request is missing id")
                    call.respond(HttpStatusCode.InternalServerError, error)
                    return@put
                }

                // Update record with given payload content
                val todo = call.receive<TodoItem>()
                val todoItem = todoService.update(id.toInt(), todo)

                if (todoItem == null) {
                    val error = PayloadError(HttpStatusCode.NotFound.value, "Request todo does not exist")
                    call.respond(HttpStatusCode.NotFound, error)
                    return@put
                }

                val payloadSuccess = PayloadSuccess(HttpStatusCode.NoContent.value, "Record updated successfully", todoItem)
                call.respond(HttpStatusCode.NoContent, payloadSuccess)
            }

            delete("todos/{id}") {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Missing id")

                val foundItem = todoService.getTodo(id.toInt())

                if (foundItem == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@delete
                }

                val isSuccess = todoService.delete(id.toInt())

                if (isSuccess) {
                    val payloadSuccess = PayloadSuccess(HttpStatusCode.OK.value, "Todo deleted successfully")
                    call.respond(HttpStatusCode.OK, payloadSuccess)
                } else {
                    val error = PayloadError(HttpStatusCode.InternalServerError.value, "Failed to delete todo")
                    call.respond(HttpStatusCode.InternalServerError, error)
                }

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}