package com.psquare.todolist.services

import com.psquare.todolist.dao.TodoItem
import com.psquare.todolist.dao.UserEntity
import com.psquare.todolist.repositories.TodoRepository

class TodoServiceImpl(private val todoRepository: TodoRepository) : TodoService {

    override suspend fun getAll(user: UserEntity): List<TodoItem> {
        return todoRepository.getAll(user)
    }

    override suspend fun getTodo(id: Int): TodoItem? {
        return todoRepository.getTodo(id)
    }

    override suspend fun create(user: UserEntity, todoItem: TodoItem): TodoItem {
        return todoRepository.create(user, todoItem)
    }

    override suspend fun update(id: Int, todoItem: TodoItem): TodoItem? {
        return todoRepository.update(id, todoItem)
    }

    override suspend fun delete(id: Int): Boolean {
        return todoRepository.delete(id)
    }
}