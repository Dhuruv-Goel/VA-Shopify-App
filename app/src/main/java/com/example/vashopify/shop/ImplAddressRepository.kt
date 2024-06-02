package com.example.vashopify.shop

import android.content.SharedPreferences
import android.util.Log
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.data.AddressRequest
import com.example.vashopify.data.AddressResponse
import com.example.vashopify.fragments.shopping.api.AddressApi
import com.example.vashopify.util.Resource
import retrofit2.HttpException
import java.lang.Exception
import java.net.HttpURLConnection
import javax.inject.Inject

class ImplAddressRepository @Inject constructor(
    private val addressApi: AddressApi
) : AddressRepository {
    override suspend fun getAddress(): Resource<AddressResponse> {
        return try {

//            val token1 = prefs.getString("jwt", null) ?: return Resource.Unauthorized()
            addressApi.getAddress()
            val response = addressApi.getAddress()
            Log.d("impl",response.toString())
            Resource.Success(response)
//                Toast.makeText(coroutineContext, "impl res error", Toast.LENGTH_SHORT).show()

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("Add error1", e.message.toString())
                Resource.Unauthorized()
            } else {
                Log.e("Add error2", e.message.toString())
                Resource.Error()
            }
        } catch (e: Exception) {
            Log.e("Add error3", e.message.toString())
            Resource.Error()
        }
    }


    override suspend fun createAddress(
        addressTitle: String,
        shopName: String,
        street: String,
        phone: String,
        city: String,
        state: String
    ): AuthResult<Unit> {
        return try {

//            val token1 = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            addressApi.createAddress(
                request = AddressRequest(
                    addressTitle = addressTitle,
                    shopName = shopName,
                    street = street,
                    phone = phone,
                    city = city,
                    state = state
                )
            )

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

    override suspend fun deleteAddress(): AuthResult<Unit> {
        return try {

//            val token1 = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            addressApi.deleteAddress()

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

    override suspend fun updateAddress(addressTitle:String,shopName:String,street:String,phone:String,city:String, state:String): AuthResult<Unit> {
        return try {

//            val token1 = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            addressApi.updateAddress(

                request = AddressRequest(
                    addressTitle = addressTitle,
                    shopName = shopName,
                    street = street,
                    phone = phone,
                    city = city,
                    state = state
                )
            )

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
}