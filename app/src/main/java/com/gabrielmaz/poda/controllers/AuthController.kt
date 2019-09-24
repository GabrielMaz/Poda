package com.gabrielmaz.poda.controllers

import com.gabrielmaz.poda.services.AuthService
import com.gabrielmaz.poda.services.request.LoginRequest
import com.gabrielmaz.poda.services.request.SignupRequest

class AuthController {
    private val authService = RetrofitController.retrofit.create(AuthService::class.java)

    suspend fun login(email: String, password: String) {
        val request = LoginRequest(email, password)
        val response = authService.login(request)

        RetrofitController.accessToken = response.authToken
    }

    suspend fun signup(name: String, email: String, password: String, passwordConfirmation: String) {
        val request = SignupRequest(name, email, password, passwordConfirmation)
        val response = authService.signup(request)

        RetrofitController.accessToken = response.authToken
    }

    suspend fun logout() {
        authService.logout()
        RetrofitController.accessToken = null
    }
}