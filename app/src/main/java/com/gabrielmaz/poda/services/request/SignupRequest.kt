package com.gabrielmaz.poda.services.request

data class SignupRequest(
    val name: String,
    val email: String,
    val password: String,
    val passwordConfirmation: String
)