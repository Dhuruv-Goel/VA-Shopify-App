package com.example.vashopify.fragments.categories.api

import com.example.vashopify.data.CategoryResponse
import com.example.vashopify.util.Constants.catapihead
import retrofit2.http.GET

interface CategoryApi {

    @GET("$catapihead/allcategories")
    suspend fun getAllCategories() : List<CategoryResponse>
}