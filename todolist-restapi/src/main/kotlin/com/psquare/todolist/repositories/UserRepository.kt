package com.psquare.todolist.repositories

import com.psquare.todolist.dao.User
import com.psquare.todolist.dao.UserEntity

interface UserRepository {

    suspend fun findUserById(id: Int): User?

    suspend fun findUserByName(name: String): UserEntity?

    suspend fun retrieveUserEntityById(id: Int): UserEntity?

    suspend fun create(user: User): User
}