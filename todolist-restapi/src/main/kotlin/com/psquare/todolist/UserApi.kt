package com.psquare.com.psquare.todolist

import com.psquare.com.psquare.todolist.models.PayloadSuccess
import com.psquare.com.psquare.todolist.models.PayloadUser
import com.psquare.todolist.auth.JwtConfig
import com.psquare.todolist.dao.User
import com.psquare.todolist.models.PayloadError
import com.psquare.todolist.services.UserService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.userApi(userService: UserService) {
    route("/user") {
        post("/register/") {
            val user = call.receive<User>()

            val foundUser = userService.findUserByName(user.name)
            // User is not registered
            if (foundUser == null) {
                val createdUser = userService.create(user)

                val token = JwtConfig.generateToken(createdUser)
                val payloadUser = PayloadUser(createdUser.id, createdUser.name, token)
                val payloadSuccess = PayloadSuccess(
                    HttpStatusCode.OK.value,
                    "User registered successfully",
                    payloadUser
                )
                call.respond(HttpStatusCode.OK, payloadSuccess)
            } else {
                // Already registered
                val payloadError = PayloadError(HttpStatusCode.BadRequest.value, "User is already exist")
                call.respond(HttpStatusCode.BadRequest, payloadError)
            }
        }

        post("/login/") {
            val user = call.receive<User>()

            val foundUser = userService.findUserByName(user.name)
            // User is not exist
            if (foundUser == null) {
                val payloadError = PayloadError(HttpStatusCode.BadRequest.value, "No such user exist")
                call.respond(HttpStatusCode.BadRequest, payloadError)
            } else {
                // User is available, create a token and revert
                val createdUser = userService.create(user)

                val token = JwtConfig.generateToken(createdUser)
                val payloadUser = PayloadUser(createdUser.id, createdUser.name, token)
                val payloadSuccess = PayloadSuccess(
                    HttpStatusCode.OK.value,
                    "User logged in",
                    payloadUser
                )
                call.respond(HttpStatusCode.OK, payloadSuccess)
            }
        }
    }
}