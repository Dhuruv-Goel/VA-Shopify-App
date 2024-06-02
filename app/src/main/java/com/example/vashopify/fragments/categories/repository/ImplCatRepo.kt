package com.example.vashopify.fragments.categories.repository

import android.util.Log
import com.example.vashopify.data.CategoryResponse
import com.example.vashopify.fragments.categories.api.CategoryApi
import com.example.vashopify.util.Resource
import retrofit2.HttpException
import java.lang.Exception
import java.net.HttpURLConnection


class ImplCatRepo(
    private val categoryApi: CategoryApi
) : CatRepo {
    override suspend fun getAllCategories(): Resource<List<CategoryResponse>> {
        return try {
            categoryApi.getAllCategories()
            val response = categoryApi.getAllCategories()
            Log.e("error", "IMPL SUCCESS")
            Resource.Success(response)
//                Toast.makeText(coroutineContext, "impl res error", Toast.LENGTH_SHORT).show()

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                Log.e("error", e.message.toString() + "   3")
                Resource.Error()
            } else {
                Log.e("error", e.message.toString() + "   2")
                Resource.Error()
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString() + "  1")
            Resource.Error()
        }
    }
}