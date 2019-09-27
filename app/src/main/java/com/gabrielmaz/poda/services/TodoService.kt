package com.gabrielmaz.poda.services

import com.gabrielmaz.poda.models.Todo
import retrofit2.http.GET
import retrofit2.http.Header

interface TodoService {
    @GET("todos")
    suspend fun getTodos(
        @Header("Authorization") authorization: String?,
        @Header("Content-Type") contentType: String
    ): ArrayList<Todo>
}