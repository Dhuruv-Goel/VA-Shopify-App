package com.example.vashopify.auth

data class AuthRequest(
    val firstName: String,
    val lastName: String,
    val mobileNumber: String,
    val email: String,
    val password: String
)
