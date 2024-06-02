package com.example.vashopify.auth

import com.example.vashopify.data.User
import com.example.vashopify.data.UserResponse
import com.example.vashopify.util.Resource
import okhttp3.MultipartBody

interface AuthRepository {
    suspend fun signUp(firstName:String,lastName: String,mobileNumber:String,email:String, password: String): AuthResult<Unit>
    suspend fun signIn(email:String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
//    suspend fun logout(): AuthResult<Unit>
//    suspend fun logoutAll(): AuthResult<Unit>
    suspend fun getAccount():Resource<UserResponse>
    suspend fun updateAccount(user: User):AuthResult<Unit>
    suspend fun uploadImage(image: MultipartBody.Part):AuthResult<Unit>
}