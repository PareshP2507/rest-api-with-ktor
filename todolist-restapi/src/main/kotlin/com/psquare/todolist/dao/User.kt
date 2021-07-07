package com.psquare.todolist.dao

import io.ktor.auth.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

data class User(val id: Int, val name: String, val password: String) : Principal

object Users : IntIdTable() {
    val name = varchar("name", 255)
    val password = varchar("password", 2500)
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)

    var name by Users.name
    var password by Users.password

    override fun toString(): String {
        return "User [id: $id, name: $name, password: $password]"
    }

    fun toUser() = User(id.value, name, password)
}