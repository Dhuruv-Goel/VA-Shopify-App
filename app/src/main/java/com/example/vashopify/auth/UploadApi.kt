package com.example.vashopify.auth

import com.example.vashopify.util.Constants
import okhttp3.MultipartBody
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadApi {
    @POST("${Constants.apihead}/uploadImage")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    )
}