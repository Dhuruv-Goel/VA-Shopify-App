package com.example.vashopify.util

interface TokenManagerInterface {

    fun saveToken(token: String)
    fun saveUserId(userId: String)
    fun getToken(): String?
    fun getUserId(): String?
}