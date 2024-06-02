package com.example.vashopify.auth
import com.example.vashopify.data.TokenResponse
import com.example.vashopify.data.User
import com.example.vashopify.data.UserResponse
import com.example.vashopify.util.Constants.apihead
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AuthApi {

    @POST("$apihead/users/register")
    suspend fun signUp(
        @Body request: AuthRequest
    )

    @POST("$apihead/users/login")
    suspend fun signIn(
        @Body request: SignInRequest
    ): TokenResponse



    @GET("$apihead/users/account")
    suspend fun getAccount(
       @Query ("userId")  userId:String
    ):UserResponse


//    @POST("/users/logoutAll")
//    suspend fun logoutAll(
//        @Header("Authorization") token: String
//    )

}