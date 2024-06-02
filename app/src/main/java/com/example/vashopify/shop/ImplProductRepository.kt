package com.example.vashopify.shop

import android.util.Log
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.data.ProductResponse
import com.example.vashopify.util.Resource
import com.google.rpc.context.AttributeContext.Auth
import retrofit2.HttpException
import java.lang.Exception
import java.net.HttpURLConnection

class ImplProductRepository(
    private val productApi: ProductApi
) : ProductRepository {
    override suspend fun getAllProducts(): Resource<List<ProductResponse>> {
        return try {
            productApi.getAllProducts()
            val response = productApi.getAllProducts()

            Resource.Success(response)
//                Toast.makeText(coroutineContext, "impl res error", Toast.LENGTH_SHORT).show()

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                Log.e("error", e.message.toString())
                Resource.Error()
            } else {
                Log.e("error", e.message.toString())
                Resource.Error()
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
            Resource.Error()
        }
    }


    override suspend fun getProductsByCategory(
        categoryName: String,
        page: Long,
        pageSize: Long
    ): Resource<List<ProductResponse>> {
        return try {
            productApi.getProductsByCategory(
                categoryName,
                page,
                pageSize

            )
            val response = productApi.getProductsByCategory(
                categoryName,
                page,
                pageSize
            )

            Resource.Success(response)

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                Log.e("error", e.message.toString())
                Resource.Error()
            } else {
                Log.e("error", e.message.toString())
                Resource.Error()
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
            Resource.Error()
        }
    }

    override suspend fun getSearchedProducts(
        query: String,
        page: Long,
        pageSize: Long
    ): Resource<List<ProductResponse>> {
        return try {
            productApi.getSearchedProducts(
                query,
                page,
                pageSize

            )
            val response = productApi.getSearchedProducts(
                query,
                page,
                pageSize
            )

            Resource.Success(response)

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                Log.e("error", e.message.toString())
                Resource.Error()
            } else {
                Log.e("error", e.message.toString())
                Resource.Error()
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
            Resource.Error()
        }
    }

    override suspend fun updateAdded(productId: String): AuthResult<Unit> {
        return try {
            productApi.updateAdded(productId)

            AuthResult.Authorized(Unit)

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                Log.e("error", e.message.toString())
                AuthResult.UnknownError()
            } else {
                Log.e("error", e.message.toString())
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
            AuthResult.UnknownError()
        }
    }

//    override suspend fun getProduct(productId: String): Resource<ProductResponse> {
//        return try {
//            productApi.getProduct(
//                productId
//            )
//            val response = productApi.getProduct(productId)
//            Resource.Success(response)
//
//        } catch (e: HttpException) {
//            if (e.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
//                Log.e("error", e.message.toString())
//                Resource.Error()
//            } else {
//                Log.e("error", e.message.toString())
//                Resource.Error()
//            }
//        } catch (e: Exception) {
//            Log.e("error", e.message.toString())
//            Resource.Error()
//        }
//    }
}