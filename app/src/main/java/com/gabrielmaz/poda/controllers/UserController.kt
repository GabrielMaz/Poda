package com.gabrielmaz.poda.controllers

import android.net.Uri
import com.gabrielmaz.poda.services.UserService
import okhttp3.MultipartBody

class UserController {
    private val userService = RetrofitController.retrofit.create(UserService::class.java)

    suspend fun getUser() = userService.getUser(RetrofitController.accessToken, "application/json")

    suspend fun setPhoto(file: MultipartBody.Part) =
        userService.setPhoto(RetrofitController.accessToken, file)


}