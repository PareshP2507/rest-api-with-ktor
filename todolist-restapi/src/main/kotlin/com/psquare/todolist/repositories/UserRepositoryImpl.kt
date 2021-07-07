package com.psquare.todolist.repositories

import com.psquare.todolist.dao.User
import com.psquare.todolist.dao.UserEntity
import com.psquare.todolist.dao.Users
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepositoryImpl : UserRepository {

    override suspend fun findUserById(id: Int): User? {
        return retrieveUserEntityById(id)?.toUser()
    }

    override suspend fun findUserByName(name: String): UserEntity? {
        return transaction {
            UserEntity.find { Users.name eq name }.firstOrNull()
        }
    }

    override suspend fun retrieveUserEntityById(id: Int): UserEntity? {
        return transaction {
            UserEntity.findById(id)
        }
    }

    override suspend fun create(user: User): User {
        return transaction {
            UserEntity.new {
                this.name = user.name
                this.password = user.password
            }
        }.toUser()
    }
}