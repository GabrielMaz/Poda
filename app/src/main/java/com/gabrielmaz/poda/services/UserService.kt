package com.gabrielmaz.poda.services

import android.content.Intent
import android.net.Uri
import com.gabrielmaz.poda.models.User
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserService {
    @GET("users/me")
    suspend fun getUser(
        @Header("Authorization") authorization: String?,
        @Header("Content-Type") contentType: String
    ): User

    @Multipart
    @PUT("users/7/avatar")
    suspend fun setPhoto(
        @Header("Authorization") authorization: String?,
        @Part file: MultipartBody.Part
    )

}
