package com.example.vashopify.fragments.categories.repository


import com.example.vashopify.data.CategoryResponse
import com.example.vashopify.util.Resource

interface CatRepo {

    suspend fun getAllCategories(): Resource<List<CategoryResponse>>
}