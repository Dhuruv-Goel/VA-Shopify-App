package com.example.vashopify.fragments.shopping.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.data.CartItemResponse
import com.example.vashopify.fragments.shopping.repository.CartRepository
import com.example.vashopify.shop.AddressRepository
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val addressRepository: AddressRepository
):ViewModel() {

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> get() = _showLoading
    private val _showLoadingAdrs = MutableLiveData<Boolean>()
    val showLoadingAdrs: LiveData<Boolean> get() = _showLoadingAdrs
    private val _getaddress = MutableStateFlow<ProductEvent>(ProductEvent.Empty)
    val getaddress: StateFlow<ProductEvent> get() = _getaddress
    //    private val resultUpdate = Channel<AuthResult<Unit>>()
//    val updateResult1 = resultUpdate.receiveAsFlow()
    private val _cartItems = MutableLiveData<List<CartItemResponse>>(emptyList())
    val cartItems: LiveData<List<CartItemResponse>> get() = _cartItems
//    private val _totalPriceState = MutableStateFlow<Int>(0)
//    val totalPriceState: StateFlow<Int> get() = _totalPriceState
//    val productsPrice: Flow<Int> = cartItems.map {
//        calculatePrice(it)
//    }
//
//    private fun calculatePrice(clist: List<CartItemResponse>): Int {
//        return clist.sumByDouble { cartItem ->
//            (cartItem.productId.ratePrice * cartItem.quantity).toDouble()
//
//        }.toInt()
//    }

    @SuppressLint("SuspiciousIndentation")
    fun getCart() {
        viewModelScope.launch {
            _showLoading.value = true

            try {
                when (val response = cartRepository.getUserCart()) {
                    is Resource.Success -> {
                        viewModelScope.launch {
                            val items = response.data?.items ?: emptyList()
//                            val tp = response.data?.totalPrice!!
//                            _totalPriceState.value=tp
                            _cartItems.value = items
                        }
                        _showLoading.value = false
                    }
                    is Resource.Error ->{
                        _cartItems.value = emptyList()
                        _showLoading.value = false
                    }

                    else -> Unit
                }
            } catch (e: Exception) {
                _showLoading.value = false
                Log.e("error", "Exception in getUserCart", e)
            }
        }
    }
    fun getAddress() {
        _showLoadingAdrs.value = true
        viewModelScope.launch {
            try {
                when (val result = addressRepository.getAddress()) {
                    is Resource.Success -> {
                        viewModelScope.launch {
                            _getaddress.value = ProductEvent.AddressSuccess(result.data!!)
                            Log.e("get", result.data.toString())
                        }
                    }

                    is Resource.Error -> {
                        viewModelScope.launch {
                            _getaddress.value = ProductEvent.Failure

                        }
                    }

                    is Resource.Unauthorized -> {
                        viewModelScope.launch {
                            _getaddress.value = ProductEvent.Unauthorised

                        }
                    }
                }
            }catch (e: Exception) {
                Log.e("error", e.message.toString() + " Get Address")
                // Handle error (e.g., show an error message)
                _getaddress.value = ProductEvent.Failure

            }
        }
    }
}