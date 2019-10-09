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

    @PUT("todos/{userId}")
    suspend fun updateTodo(
        @Header("Authorization") authorization: String?,
        @Header("Content-Type") contentType: String,
        @Path("userId") userId: Int,
        @Body todoRequest: TodoRequest
    ) :SuccessReponse
}