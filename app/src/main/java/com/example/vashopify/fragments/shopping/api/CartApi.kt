package com.example.vashopify.fragments.shopping.api

import com.example.vashopify.data.CartItem
import com.example.vashopify.data.CartResponse
import com.example.vashopify.util.Constants.cartapihead
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface CartApi {
    @POST("$cartapihead/add")
    suspend fun addProductToCart(
//        @Header("Authorization") token: String,
        @Body cartItem: CartItem
    )


    @GET("$cartapihead/view")
    suspend fun getUserCart(
//        @Header("Authorization") token: String
    ): CartResponse


    @PUT("$cartapihead/update")
    suspend fun updateQuantity(
//        @Header("Authorization") token: String,
        @Query("productId") productId:String,
        @Body quantity: String
    )

    @PUT("$cartapihead/remove")
    suspend fun deleteProductFromCart(
//        @Header("Authorization") token: String,
        @Query("productId") productId:String
    ):CartResponse
    @PUT("$cartapihead/tp")
    suspend fun updateTotalPrice(
//        @Header("Authorization") token: String,
        @Query("totalPrice") totalPrice:Int
    ):CartResponse

    @PUT("$cartapihead/increase")
    suspend fun increaseQuantity(
//        @Header("Authorization") token: String,
        @Query("productId") productId:String

    ):CartResponse

    @PUT("$cartapihead/decrease")
    suspend fun decreaseQuantity(
//        @Header("Authorization") token: String,
        @Query("productId") productId:String
    ):CartResponse

}