package com.example.vashopify.data

data class UserResponse(
    val _id: String,
    val firstName: String,
    val lastName: String,
    val mobileNumber: String,
    val email: String,
    val password: String,
    val profileImageUrl: String,
)