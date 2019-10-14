package com.gabrielmaz.poda.services

import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.poda.services.request.TodoRequest
import com.gabrielmaz.poda.services.response.SuccessReponse
import retrofit2.http.*

interface TodoService {
    @GET("todos")
    suspend fun getTodos(
        @Header("Authorization") authorization: String?,
        @Header("Content-Type") contentType: String
    ): ArrayList<Todo>

    @PUT("todos/{id}")
    suspend fun updateTodo(
        @Header("Authorization") authorization: String?,
        @Header("Content-Type") contentType: String,
        @Path("id") id: Int,
        @Body todoRequest: TodoRequest
    ): SuccessReponse
}