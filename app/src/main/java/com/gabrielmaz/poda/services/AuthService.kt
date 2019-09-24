package com.gabrielmaz.poda.services

import com.gabrielmaz.poda.services.request.LoginRequest
import com.gabrielmaz.poda.services.request.SignupRequest
import com.gabrielmaz.poda.services.response.RequestResponse
import com.gabrielmaz.poda.services.response.SuccessReponse
import com.gabrielmaz.poda.services.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): TokenResponse

    @POST("auth/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): TokenResponse

    @POST("auth/logout")
    suspend fun logout(): SuccessReponse
}