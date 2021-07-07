package com.psquare.todolist.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.psquare.todolist.dao.User
import java.util.*

object JwtConfig {

    private const val SECRET = "this-is-secret"
    private const val ISSUER = "ktor.io"
    private const val VALIDITY_MS = 36_00_000 * 10 // 10 hours
    private val ALGO = Algorithm.HMAC512(SECRET)

    val verifier: JWTVerifier = JWT
        .require(ALGO)
        .withIssuer(ISSUER)
        .build()

    fun generateToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(ISSUER)
        .withClaim("id", user.id)
        .withExpiresAt(buildExpiration())
        .sign(ALGO)

    private fun buildExpiration() = Date(System.currentTimeMillis() + VALIDITY_MS)
}