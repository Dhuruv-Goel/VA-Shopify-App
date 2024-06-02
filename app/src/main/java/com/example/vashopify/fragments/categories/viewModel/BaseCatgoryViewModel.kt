package com.example.vashopify.fragments.categories.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.shop.ProductRepository
import com.example.vashopify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseCatgoryViewModel @Inject constructor(
    private val productRepository: ProductRepository
//    private val category:CategoryResponse
) : ViewModel() {
    private val pagingInfo = PagingInfo()
    private val _baseState =
        MutableStateFlow<ProductEvent>(ProductEvent.Empty)
    val baseState: StateFlow<ProductEvent> get() = _baseState

    fun fetchProducts(categoryName: String) {
        viewModelScope.launch {
            _baseState.value = ProductEvent.Loading

            when (val productResponse = productRepository.getProductsByCategory(
                categoryName,
                page = pagingInfo.page, pageSize = pagingInfo.page * 8
            )) {
                is Resource.Error -> {
                    Log.e("error", "Resource Error Base Category")
                    viewModelScope.launch {

                        _baseState.value = ProductEvent.Failure
                    }
                }

                is Resource.Success -> {
                    val list = productResponse.data!!
                    Log.d("error", "Resource Success Base Category")
//                    Log.d("error",list.toString())
                    viewModelScope.launch {

                        _baseState.value = ProductEvent.Success(list)
                    }
                   pagingInfo.page++
                }

                else -> Unit
            }
        }
    }

    internal data class PagingInfo(
        var page: Long = 1

    )
}