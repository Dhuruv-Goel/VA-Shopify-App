package com.example.vashopify.auth

import android.util.Log
import com.example.vashopify.data.User
import com.example.vashopify.data.UserResponse
import com.example.vashopify.util.Resource
import com.example.vashopify.util.TokenManager
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val userApi: UserApi,
   private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        mobileNumber: String,
        email: String,
        password: String
    ): AuthResult<Unit> {
        return try {
            api.signUp(
                request = AuthRequest(
                    firstName = firstName,
                    lastName = lastName,
                    mobileNumber = mobileNumber,
                    email = email,
                    password = password
                )
            )
            AuthResult.Authorized(Unit)
        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", "sign up unauthorised")
                AuthResult.Unauthorized()
            } else {
                Log.e("error", " sign up auth repo uncommon")
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.e("error", "authImpl unco")
            AuthResult.UnknownError()
        }
    }


    override suspend fun signIn(email: String, password: String): AuthResult<Unit> {
        return try {
            api.signIn(
                request = SignInRequest(
                    email = email,
                    password = password
                )
            )
            val response = api.signIn(
                request = SignInRequest(
                    email = email,
                    password = password
                )
            )
//            prefs.edit()
//                .putString(USER_TOKEN, response.token)
//                .putString(USER_ID, response.userId)
//                .apply()
            Log.d("token", response.token.toString())
            tokenManager.saveToken(token = response.token)
            tokenManager.saveUserId(userId = response.userId)
            AuthResult.Authorized(Unit)
        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", "sign in unauthorised")

                AuthResult.Unauthorized()
            } else {
                Log.e("error", " sign in auth repo uncommon")

                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            Log.e("error", " authen authorised")

//            val token = tokenManager.getToken() ?: return AuthResult.Unauthorized()
            userApi.authenticate()
//            Log.e("error", token)

            AuthResult.Authorized(Unit)

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", " authenticate unauthorised")

                AuthResult.Unauthorized()
            } else {
                Log.e("error", " authenticate auth repo uncommon")

                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

//    override suspend fun logout(): AuthResult<Unit> {
//     return try {
////            Log.e("error"," authen authorised")
////           val token = tokenManager.getToken()?: return AuthResult.Unauthorized()
//         userApi.logout()
//         tokenManager.saveToken("")
//         tokenManager.saveUserId("")
////            Log.e("error",token)
//
//            AuthResult.Authorized(Unit)
//
//        } catch (e: HttpException) {
//            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                Log.e("error", " authenticate unauthorised")
//
//                AuthResult.Unauthorized()
//            } else {
//                Log.e("error", " authenticate auth repo uncommon")
//
//                AuthResult.UnknownError()
//            }
//        } catch (e: Exception) {
//            AuthResult.UnknownError()
//        }
//    }

//    override suspend fun logoutAll(): AuthResult<Unit> {
//        return AuthResult.UnknownError()
//
//    }

    override suspend fun getAccount(): Resource<UserResponse> {
        return try {
//            Log.e("error"," authen authorised")

            val userID = tokenManager.getUserId() ?: return Resource.Unauthorized()
//            api.getAccount("Bearer $token")
            val response = api.getAccount(userID)
//            Log.d("token", token)
//            Log.d("token", response.toString())
            Resource.Success(response)

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", " authenticate unauthorised")

                Resource.Unauthorized()
            } else {
                Log.e("error", " authenticate auth repo uncommon")

                Resource.Error()
            }
        } catch (e: Exception) {
            Resource.Error()
        }
    }

    override suspend fun updateAccount(user: User): AuthResult<Unit> {
        return try {
//            Log.e("error"," authen authorised")

//            val token = tokenManager.getToken() ?: return AuthResult.Unauthorized()
            userApi.updateAccount( request = user)
//            Log.e("error",token)

            AuthResult.Authorized(Unit)

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", " authenticate unauthorised")

                AuthResult.Unauthorized()
            } else {
                Log.e("error", " authenticate auth repo uncommon")

                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun uploadImage(image: MultipartBody.Part): AuthResult<Unit> {
        return try {
//            val token = tokenManager.getToken() ?: return AuthResult.Unauthorized()
            userApi.uploadImage(image)

            AuthResult.Authorized(Unit)

        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e("error", " uploadImage unauthorised")

                AuthResult.Unauthorized()
            } else {
                Log.e("error", " uploadImage auth repo uncommon")

                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.e("error", " uploadImage auth repo catch")

            AuthResult.UnknownError()
        }
    }
}