package com.psquare.todolist.repositories

import com.psquare.todolist.dao.TodoItem
import com.psquare.todolist.dao.UserEntity

interface TodoRepository {

    suspend fun getAll(user: UserEntity) : List<TodoItem>

    suspend fun getTodo(id: Int): TodoItem?

    suspend fun create(user: UserEntity, todoItem: TodoItem): TodoItem

    suspend fun update(id: Int, todoItem: TodoItem): TodoItem?

    suspend fun delete(id:Int): Boolean
}