package com.example.vashopify.auth

import com.example.vashopify.data.User
import com.example.vashopify.util.Constants
import com.example.vashopify.util.Constants.apihead
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserApi {

    @GET("${Constants.apihead}/users/profile")
    suspend fun authenticate(
//        @Header("Authorization") token: String
    )

    @Multipart
    @POST("${apihead}/uploadImage")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    )

    @PUT("${Constants.apihead}/users/update")
    suspend fun updateAccount(
//        @Header("Authorization") token: String,
        @Body request: User
    )

//    @POST("${Constants.apihead}/users/logout")
//    suspend fun logout(
////        @Header("Authorization") token: String
//    )

}