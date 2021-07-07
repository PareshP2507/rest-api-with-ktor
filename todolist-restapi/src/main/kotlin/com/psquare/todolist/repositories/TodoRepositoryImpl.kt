package com.psquare.todolist.repositories

import com.psquare.todolist.dao.*
import org.jetbrains.exposed.sql.transactions.transaction

class TodoRepositoryImpl : TodoRepository {

    override suspend fun getAll(user: UserEntity): List<TodoItem> {
        val todos = mutableListOf<TodoItem>()
        transaction {
            todos.addAll(TodoEntity.find { Todos.user eq user.id}.map { it.toTodo() })
        }
        return todos
    }

    override suspend fun getTodo(id: Int): TodoItem? {
        return transaction {
            TodoEntity.findById(id)
        }?.toTodo()
    }

    override suspend fun create(user: UserEntity, todoItem: TodoItem): TodoItem {
        return transaction {
            TodoEntity.new {
                title = todoItem.title
                details = todoItem.details
                dueDate = todoItem.dueDate
                importance = todoItem.importance
                this.user = user
            }
        }.toTodo()
    }

    override suspend fun update(id: Int, todoItem: TodoItem): TodoItem? {
        val transaction = transaction {
            TodoEntity.findById(id)?.apply {
                title = todoItem.title
                details = todoItem.details
                importance = todoItem.importance
            }
        }
        return transaction?.toTodo()
    }

    override suspend fun delete(id: Int): Boolean {
        var isSuccess = false
        try {
            transaction { TodoEntity.findById(id)?.delete() }
            isSuccess = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isSuccess
    }
}