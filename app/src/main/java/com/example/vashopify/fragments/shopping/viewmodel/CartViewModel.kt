package com.example.vashopify.fragments.shopping.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.vashopify.activities.ShoppingActivity
import com.example.vashopify.data.CartItemResponse
import com.example.vashopify.data.CartResponse
import com.example.vashopify.fragments.shopping.repository.CartRepository
import com.example.vashopify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
//    private val productRepository: ProductRepository
) : ViewModel() {

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> get() = _showLoading

    //    private val resultUpdate = Channel<AuthResult<Unit>>()
//    val updateResult1 = resultUpdate.receiveAsFlow()
    private val _cartItems = MutableStateFlow<CartResponse>(CartResponse("","", emptyList(),0,0))
    val cartItems: StateFlow<CartResponse> get() = _cartItems.asStateFlow()

    //    val productsPrice = cartState.map {
//        when (it) {
//            is ProductEvent.CartSucess -> {
//                calculatePrice(it.clist)
//            }
//
//            else -> null
//        }
//    }

//    init {
//         getCart()
//    }
    val productsPrice: Flow<Int> = cartItems.map {
        calculatePrice(it.items)
    }

    private fun calculatePrice(clist: List<CartItemResponse>): Int {
        return clist.sumByDouble { cartItem ->
            (cartItem.productId.ratePrice!! * cartItem.quantity).toDouble()

        }.toInt()
    }


    @SuppressLint("SuspiciousIndentation")
    fun getCart() {
        viewModelScope.launch {
            _showLoading.value = true
            try {
                when (val response = cartRepository.getUserCart()) {
                    is Resource.Success -> {
                        viewModelScope.launch {
                            val items = response.data!!
                            _cartItems.value = items
                            _showLoading.value = false
                        }
                    }
                    is Resource.Error ->{
//                         _cartItems.value = emptyList()
                        viewModelScope.launch {
                            _showLoading.value = false
                        }
                    }
                    else -> Unit
                }
            } catch (e: Exception) {
                _showLoading.value = false
                Log.e("error", "Exception in getUserCart", e)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun updateTotalPrice(totalPrice: Int) {
        viewModelScope.launch {
            try {
               cartRepository.updateTotalPrice(totalPrice)
            } catch (e: Exception) {
                _showLoading.value = false
                Log.e("error", "Exception in updateTotalPrice", e)
            }
        }
    }
//        val productsPrice = cartState.map {
//            when (it) {
//                is ProductEvent.CartSucess -> {
//                    calculatePrice(it.clist)
//                }
//
//                else -> null
//            }
//        }


//    fun updateAdded(productId: String, isAdded: Boolean) {
//        viewModelScope.launch {
//            _showLoading.value = true
//            try {
//                val response = productRepository.updateAdded(productId, isAdded)
//                Log.d("error", response.toString())
//                resultUpdate.send(response)
//            } catch (e: Exception) {
//                // Handle authentication failure
//                resultUpdate.send(AuthResult.UnknownError())
//            }
//        }
//
//    }


    fun increase(position: Int) {
        viewModelScope.launch {
            _cartItems.value.let { cartItems ->
                val cartResponse = cartItems.items.getOrNull(position)
                cartResponse?.let {
                    cartRepository.increaseQuantity(it.productId._id)
                    getCart()
                }
            }

        }
//            val item = _cartState.value.apply {
//            }
//            val result = cartRepository.increaseQuantity(productId)
//            handleResult(result,position)
    }

    fun decrease(position: Int) {
        viewModelScope.launch {
            _cartItems.value.let { cartItems ->
                val cartResponse = cartItems.items.getOrNull(position)
                cartResponse?.let {
                    cartRepository.decreaseQuantity(it.productId._id.toString())
                    getCart()
                }
            }

        }
    }

    fun delete(position: Int) {
        viewModelScope.launch {
            _cartItems.value.let { cartItems ->
                val cartResponse = cartItems.items.getOrNull(position)
                cartResponse?.let {
                    val prdId = it.productId._id
                    Log.d("error", "decrease $prdId")
                    cartRepository.deleteProductFromCart(it.productId._id)
                    getCart()
                }
            }

        }
    }
}

//    private fun handleResult(result: Resource<CartResponse>,position:Int) {
//        when (result) {
//            is Resource.Success -> {
//                viewModelScope.launch {
//                    val items = result.data?.items ?: emptyList()
//
//                    val item = result.data.items.get(position)
//                    _cartState.value = ProductEvent.CartSucess(items)
//                }
//                // Successfully authorized, you can update UI or take other actions if needed
//            }
//
//            else -> Unit
//        }
//    }

