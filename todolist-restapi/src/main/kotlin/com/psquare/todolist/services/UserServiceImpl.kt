package com.psquare.todolist.services

import com.psquare.todolist.dao.User
import com.psquare.todolist.dao.UserEntity
import com.psquare.todolist.repositories.UserRepository

class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override suspend fun findUserById(id: Int): User? {
        return userRepository.findUserById(id)
    }

    override suspend fun findUserByName(name: String): UserEntity? {
        return userRepository.findUserByName(name)
    }

    override suspend fun retrieveUserEntityById(id: Int): UserEntity? {
        return userRepository.retrieveUserEntityById(id)
    }

    override suspend fun create(user: User): User {
        return userRepository.create(user)
    }
}