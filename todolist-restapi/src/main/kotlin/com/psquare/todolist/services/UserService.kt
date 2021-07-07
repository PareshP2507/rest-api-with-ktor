package com.psquare.todolist.services

import com.psquare.todolist.dao.User
import com.psquare.todolist.dao.UserEntity

interface UserService {

    suspend fun findUserById(id: Int): User?

    suspend fun findUserByName(name: String): UserEntity?

    suspend fun retrieveUserEntityById(id: Int): UserEntity?

    suspend fun create(user: User): User
}