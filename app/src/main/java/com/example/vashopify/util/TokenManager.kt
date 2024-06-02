package com.example.vashopify.util

import android.content.Context
import android.content.SharedPreferences
import com.example.vashopify.util.Constants.USER_ID
import com.example.vashopify.util.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(private var prefs :SharedPreferences) {
//    private var prefs: SharedPreferences =
//        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }
    fun saveUserId(userId: String) {
        val editor = prefs.edit()
        editor.putString(USER_ID, userId)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
    fun getUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

}