package com.gabrielmaz.poda.controllers

import com.gabrielmaz.poda.services.UserService

class UserController {
    private val userService = RetrofitController.retrofit.create(UserService::class.java)

    suspend fun getUser() = userService.getUser(RetrofitController.accessToken, "application/json")
}