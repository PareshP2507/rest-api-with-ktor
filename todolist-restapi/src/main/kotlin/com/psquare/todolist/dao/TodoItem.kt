package com.psquare.todolist.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

data class TodoItem(
        val id: Int?,
        val title: String,
        val details: String,
        val dueDate: Long = System.currentTimeMillis(),
        val importance: Importance
)

enum class Importance {
        LOW, MEDIUM, HIGH
}

object Todos : IntIdTable() {
        val title = varchar("title", 250)
        val details = varchar("details", 2500)
        val dueDate = long("due-date")
        val importance = enumeration("importance", Importance::class)
        val user = reference("user", Users)
}

class TodoEntity(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<TodoEntity>(Todos)

        var title by Todos.title
        var details by Todos.details
        var dueDate by Todos.dueDate
        var importance by Todos.importance
        var user by UserEntity referencedOn Todos.user

        override fun toString(): String {
                return "Todo[$title, $details, $dueDate, $importance, ${user.id}]"
        }

        fun toTodo() = TodoItem(id.value, title, details, dueDate, importance)
}