package com.example.vashopify.fragments.settings.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.data.OrderRequest
import com.example.vashopify.fragments.shopping.repository.CartRepository
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    private val _order = MutableStateFlow<ProductEvent>(ProductEvent.Empty)
    val order: StateFlow<ProductEvent> get() = _order.asStateFlow()
//    private val _showLoadingO = MutableLiveData<Boolean>()
//    val showLoadingO: LiveData<Boolean> get() = _showLoadingO

    @SuppressLint("SuspiciousIndentation")
     fun placeOrder(order:OrderRequest){
        viewModelScope.launch {
                _order.value = ProductEvent.Loading
            try {
                when (cartRepository.placeOrder(order)) {
                    is AuthResult.Authorized -> {
                        viewModelScope.launch {
                            _order.value = ProductEvent.UnitSuccess(Unit)
                        }
                    }
                    is AuthResult.Unauthorized ->{
                        _order.value = ProductEvent.Unauthorised
                    }
                    is AuthResult.UnknownError ->{
                        _order.value = ProductEvent.Failure
                    }
                }
            } catch (e: Exception) {
                _order.value = ProductEvent.Failure
                Log.e("error", "Exception in placeOrder", e)
            }
        }

    }


    fun getAllOrders(){
        viewModelScope.launch {
            _order.value = ProductEvent.Loading
            try {
                when (val response = cartRepository.getAllOrders()) {
                    is Resource.Success -> {

                        val data = response.data!!
                        viewModelScope.launch {
                            _order.value = ProductEvent.OrderSuccess(data)
                        }
                    }
                    is Resource.Unauthorized ->{
                        _order.value = ProductEvent.Unauthorised
                    }
                    is Resource.Error ->{
                        _order.value = ProductEvent.Failure
                    }
                }
            } catch (e: Exception) {
                _order.value = ProductEvent.Failure
                Log.e("error", "Exception in placeOrder", e)
            }
        }
    }

}