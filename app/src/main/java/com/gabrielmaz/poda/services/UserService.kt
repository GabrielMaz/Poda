package com.gabrielmaz.poda.services

import com.gabrielmaz.poda.models.User
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserService {
//    @GET("user/{userId}")
//    suspend fun getUser(@Path("userId") userId: Int): User
    @GET("user/5")
    suspend fun getUser(
        @Header("Authorization") authorization: String?,
        @Header("Content-Type") contentType: String): User

}
