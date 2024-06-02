package com.example.vashopify.fragments.shopping.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.auth.AuthRepository
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
):ViewModel() {

    private val _user = MutableStateFlow<ProductEvent>(ProductEvent.Empty)
    val user: StateFlow<ProductEvent> get() = _user.asStateFlow()
//    private val _logOut = MutableStateFlow<ProductEvent>(ProductEvent.Empty)
//    val logOut: StateFlow<ProductEvent> get() = _logOut.asStateFlow()

//    init {
//        getAccount()
//    }


    fun getAccount() {
        viewModelScope.launch {
            _user.value = ProductEvent.Loading

            try {
                when (val result = authRepository.getAccount()) {
                    is Resource.Success -> {
                        viewModelScope.launch   {
                            val user = result.data!!
                            Log.d("user", user.toString())
                            _user.value = ProductEvent.AuthSuccess(user)
                        }
                    }

                    is Resource.Unauthorized -> {
                        _user.value = ProductEvent.Unauthorised

                    }

                    is Resource.Error -> {
                        _user.value = ProductEvent.Failure

                    }
                }
            } catch (e: Exception) {
                _user.value = ProductEvent.Failure
                Log.e("errorFetch", "Expetion in get Account", e)
            }
        }
    }

//    fun logOut(){
//        viewModelScope.launch {
//            _logOut.value = ProductEvent.Loading
//            try {
//                when (authRepository.logout()) {
//                    is AuthResult.Authorized -> {
//                        _logOut.value = ProductEvent.UnitSuccess(Unit)
//                    }
//
//                    is AuthResult.Unauthorized -> {
//                        _logOut.value = ProductEvent.Unauthorised
//
//                    }
//
//                    is AuthResult.UnknownError -> {
//                        _logOut.value = ProductEvent.Failure
//
//                    }
//                }
//            } catch (e: Exception) {
//                _user.value = ProductEvent.Failure
//                Log.e("errorFetch", "Expetion in get Account", e)
//            }
//
//        }
//
//    }

}