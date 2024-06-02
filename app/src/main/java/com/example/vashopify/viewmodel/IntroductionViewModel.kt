package com.example.vashopify.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.R
import com.example.vashopify.auth.AuthRepository
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.util.Constants.INTRODUCTION_KEY
import com.example.vashopify.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int> get() = _navigate

    companion object {
        const val SHOPPING_ACTIVITY = 23
        val ACCOUNT_OPTIONS_FRAGMENT = R.id.action_introductionFragment_to_accountOptionsFragment
    }

    init {
        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)

        viewModelScope.launch {
//            try {
//                when (repository.authenticate()) {
//                    is AuthResult.Authorized -> {
//                        _navigate.value = SHOPPING_ACTIVITY
//                    }
//
//                    is AuthResult.Unauthorized -> {
//                        if (isButtonClicked) {
//                            _navigate.value = ACCOUNT_OPTIONS_FRAGMENT
//                        }
//                    }
//
//                    is AuthResult.UnknownError -> {
//                        if (isButtonClicked) {
//                            _navigate.value = ACCOUNT_OPTIONS_FRAGMENT
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                if (isButtonClicked) {
//                    _navigate.value = ACCOUNT_OPTIONS_FRAGMENT
//                }
//
//
//            }


            val token = tokenManager.getToken()
            if (token != null) {
                Log.d("token",token)
            }
            if(token!=null){
                _navigate.value = SHOPPING_ACTIVITY
            }else{
                if (isButtonClicked) {
                            _navigate.value = ACCOUNT_OPTIONS_FRAGMENT
                        }
            }
        }
    }


    fun startButtonClick() {
         sharedPreferences.edit().putBoolean(INTRODUCTION_KEY,true).apply()

    }
}




