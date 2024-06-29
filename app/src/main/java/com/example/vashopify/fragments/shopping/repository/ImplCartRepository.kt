package com.example.vashopify.fragments.shopping.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.data.CartItem
import com.example.vashopify.data.CartResponse
import com.example.vashopify.data.OrderRequest
import com.example.vashopify.fragments.shopping.api.CartApi
import com.example.vashopify.fragments.settings.api.OrderApi
import com.example.vashopify.fragments.settings.data.OrderDataResponseItem
import com.example.vashopify.util.Resource
import com.example.vashopify.util.TokenManager
import retrofit2.HttpException
import java.lang.Exception
import java.net.HttpURLConnection

class ImplCartRepository(private val cartApi: CartApi, private val orderApi: OrderApi
) :
    CartRepository
{

    override suspend fun addProductToCart(cartItem: CartItem): AuthResult<Unit> {

        return try {

//            val token1 = tokenManager.getToken()?: return AuthResult.Unauthorized()
//            Log.d("error",token1)
            cartApi.addProductToCart( cartItem)

//            Log.d("error",token1)
            AuthResult.Authorized(Unit)
//                Toast.makeText(coroutineContext, "impl res error", Toast.LENGTH_SHORT).show()

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", e.message.toString())
                AuthResult.Unauthorized()
            } else {
                Log.e("error", e.message.toString())
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
            AuthResult.UnknownError()
        }

    }

    override suspend fun getUserCart(): Resource<CartResponse> {
        return try {

//            val token1 =tokenManager.getToken()?: return Resource.Unauthorized()
            cartApi.getUserCart()
            val response = cartApi.getUserCart()
            Resource.Success(response)
//                Toast.makeText(coroutineContext, "impl res error", Toast.LENGTH_SHORT).show()

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", e.message.toString())
                Resource.Unauthorized()
            } else {
                Log.e("error", e.message.toString())
                Resource.Error()
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
            Resource.Error()
        }
    }

    override suspend fun updateQuantity(
        productId: String,
        quantity: String
    ): AuthResult<Unit> {

        return try {

//            val token1 =tokenManager.getToken() ?: return AuthResult.Unauthorized()
            cartApi.updateQuantity(productId,quantity)

            AuthResult.Authorized(Unit)
//                Toast.makeText(coroutineContext, "impl res error", Toast.LENGTH_SHORT).show()

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", e.message.toString())
                AuthResult.Unauthorized()
            } else {
                Log.e("error", e.message.toString())
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
            AuthResult.UnknownError()
        }
    }

    override suspend fun deleteProductFromCart( productId: String): Resource<CartResponse> {
        return try {

//            val token1 = prefs.getString("jwt", null) ?: return Resource.Unauthorized()
            cartApi.deleteProductFromCart(productId)
            val response = cartApi.deleteProductFromCart(productId)

           Resource.Success(response)
//                Toast.makeText(coroutineContext, "impl res error", Toast.LENGTH_SHORT).show()

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", e.message.toString())
                Resource.Unauthorized()
            } else {
                Log.e("error", e.message.toString())
                Resource.Error()
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
            Resource.Error()
        }
    }

    override suspend fun increaseQuantity(productId: String): Resource<CartResponse> {
        return try {

//            val token1 = prefs.getString("jwt", null) ?: return Resource.Unauthorized()
            cartApi.increaseQuantity(productId)
            val response = cartApi.increaseQuantity(productId)
            Resource.Success(response)
//                Toast.makeText(coroutineContext, "impl res error", Toast.LENGTH_SHORT).show()

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", e.message.toString())
                Resource.Unauthorized()
            }
            else {
                Log.e("error", e.message.toString()+ "ince else")
                Resource.Error()
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
            Resource.Error()
        }
    }

    override suspend fun updateTotalPrice(totalPrice: Int): Resource<CartResponse> {
        return try {

//            val token1 = prefs.getString("jwt", null) ?: return Resource.Unauthorized()
            cartApi.updateTotalPrice(totalPrice)
            val response = cartApi.updateTotalPrice(totalPrice)

            Resource.Success(response)
//                Toast.makeText(coroutineContext, "impl res error", Toast.LENGTH_SHORT).show()

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", e.message.toString())
                Resource.Unauthorized()
            } else {
                Log.e("error", e.message.toString())
                Resource.Error()
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
            Resource.Error()
        }
    }

    override suspend fun decreaseQuantity(productId: String): Resource<CartResponse> {
        return try {

//           val token1 = prefs.getString("jwt", null) ?: return Resource.Unauthorized()
            cartApi.decreaseQuantity(productId)
            val response = cartApi.decreaseQuantity(productId)
            Resource.Success(response)
//                Toast.makeText(coroutineContext, "impl res error", Toast.LENGTH_SHORT).show()

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", e.message.toString() + "unauth")
                Resource.Unauthorized()
            } else {
                Log.e("error", e.message.toString() + "else")
                Resource.Error()
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString() + "catch")
            Resource.Error()
        }
    }

    override suspend fun placeOrder(request: OrderRequest): AuthResult<Unit> {
        return try {
//            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()

            orderApi.placeOrder(request)

            AuthResult.Authorized(Unit)

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", "Unauthorized: ${e.message}", e)
                AuthResult.Unauthorized()
            } else {
                Log.e("error", "HTTP Error: ${e.code()}, ${e.message}", e)
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.e("error", "Unknown Error: ${e.message}", e)
            AuthResult.UnknownError()
        }
    }


    override suspend fun getAllOrders(): Resource<List<OrderDataResponseItem>> {
        return try {

//            val token1 = prefs.getString("jwt", null) ?: return Resource.Unauthorized()
            orderApi.getAllOrder()
            val response = orderApi.getAllOrder()
            Log.d("order",response.toString())
            Resource.Success(response)
//                Toast.makeText(coroutineContext, "impl res error", Toast.LENGTH_SHORT).show()

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("order", e.message.toString())
                Resource.Unauthorized()
            } else {
                Log.e("order", e.message.toString())
                Resource.Error()
            }
        } catch (e: Exception) {
            Log.e("order", e.message.toString())
            Resource.Error()
        }
    }


}