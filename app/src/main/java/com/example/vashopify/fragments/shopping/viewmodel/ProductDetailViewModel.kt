package com.example.vashopify.fragments.shopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.data.CartItem
import com.example.vashopify.fragments.shopping.repository.CartRepository
import com.example.vashopify.shop.ProductRepository
import com.example.vashopify.util.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _showLoading = MutableStateFlow<Boolean>(false)
    val showLoading: StateFlow<Boolean> get() = _showLoading.asStateFlow()
//    private val _isAddedState = MutableLiveData<Boolean>()
//    val isAddedState: LiveData<Boolean> get() = _isAddedState

    private val resultChannel = Channel<AuthResult<Unit>>()
    val addToCartResult = resultChannel.receiveAsFlow()
    private val resultUpdate = Channel<AuthResult<Unit>>()
    val updateResult = resultUpdate.receiveAsFlow()
    fun addProductToCart(productId: String) {
        viewModelScope.launch {
            _showLoading.value = true
            try {
                val response = cartRepository.addProductToCart(
                    cartItem = CartItem(
                        productId = productId,
                        quantity = 1
                    )
                )
                resultChannel.send(response)
            } catch (e: Exception) {
                resultChannel.send(AuthResult.UnknownError())
            }
        }
    }

    fun updateAdded(productId: String) {
        viewModelScope.launch {
            _showLoading.value = true
            try {
                val response = productRepository.updateAdded(productId)
                Log.d("error",response.toString())
                resultUpdate.send(response)
            } catch (e: Exception) {
                resultUpdate.send(AuthResult.UnknownError())
            }
        }

    }

//    fun getProduct(productId: String) {
//        viewModelScope.launch {
//            try {
//                when (val response = productRepository.getProduct(productId)) {
//                    is Resource.Success -> {
//                        val value = response.data?.isAdded
//                        _isAddedState.value = value!!
//                    }
//
//                    else -> {}
//                }
//            } catch (e: Exception) {
//                // Handle authentication failure
//            }
//        }
//
//    }
}