package com.psquare.todolist.models

import io.ktor.http.*

data class PayloadError (val code: Int, val message: String) {

    companion object {
        private const val INTERNAL_SERVER_ERROR = "Internal server error"

        fun buildErrorFor500() = PayloadError(HttpStatusCode.InternalServerError.value, INTERNAL_SERVER_ERROR)
    }
}
