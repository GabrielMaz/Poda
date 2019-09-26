package com.gabrielmaz.poda.controllers

import com.gabrielmaz.poda.services.TodoService

class TodoController {
    private val todoService = RetrofitController.retrofit.create(TodoService::class.java)

    suspend fun getTodos() = todoService.getTodos(RetrofitController.accessToken, "application/json")
}