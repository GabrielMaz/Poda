package com.gabrielmaz.poda.services

import com.gabrielmaz.poda.models.User
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserService {
    @GET("users/me")
    suspend fun getUser(
        @Header("Authorization") authorization: String?,
        @Header("Content-Type") contentType: String
    ): User

}
