package com.gabrielmaz.poda.controllers

import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.poda.models.TodoListItem
import com.gabrielmaz.poda.services.TodoService
import com.gabrielmaz.poda.services.request.TodoRequest
import org.threeten.bp.ZonedDateTime

class TodoController {
    private val todoService = RetrofitController.retrofit.create(TodoService::class.java)

    suspend fun getTodos() =
        todoService.getTodos(RetrofitController.accessToken, "application/json")

    suspend fun createTodo(description: String, categoryId: Int, priority: String, dueDate: ZonedDateTime) {
        val todoRequest = TodoRequest(
            priority,
            description,
            dueDate,
            false,
            categoryId
        )
        todoService.createTodo(
            "application/json",
            todoRequest
        )
    }

    suspend fun updateTodo(todo: Todo, id: Int) {

        val todoRequest = TodoRequest(
            todo.priority,
            todo.description,
            todo.dueDate,
            todo.completed,
            todo.categoryId
        )
        todoService.updateTodo(
            RetrofitController.accessToken,
            "application/json",
            id,
            todoRequest
        )
    }


    fun getTotalTasks(todos: List<Todo>): HashMap<String, Int> {
        var tasks = HashMap<String, Int>()

        todos.forEach { todo ->
            tasks[todo.category.name] = tasks[todo.category.name]?.plus(1) ?: 1
        }

        return tasks
    }

    fun getCompletedTasks(todos: List<Todo>): HashMap<String, Int> {
        val tasksCompleted = HashMap<String, Int>()
        todos.forEach { todo ->
            if (todo.completed) tasksCompleted[todo.category.name] =
                tasksCompleted[todo.category.name]?.plus(1) ?: 1
        }

        return tasksCompleted
    }

    fun getTodoWithHeaders(todos: List<Todo>): ArrayList<TodoListItem> {
        val result = ArrayList<TodoListItem>()

        if (todos.isNotEmpty()) {

            val todosSorted = todos.sortedBy { t -> t.id }.sortedBy { t -> t.dueDate }

            var currentDate: ZonedDateTime? = null

            todosSorted.forEach { todo ->

                if (currentDate == null || (todo.dueDate.year != currentDate!!.year || todo.dueDate.dayOfYear != currentDate!!.dayOfYear)) {
                    val header = TodoListItem(null, todo.dueDate)
                    result.add(header)

                    currentDate = todo.dueDate
                }

                val todoRow = TodoListItem(todo, null)
                result.add(todoRow)
            }
        }

        return result
    }
}