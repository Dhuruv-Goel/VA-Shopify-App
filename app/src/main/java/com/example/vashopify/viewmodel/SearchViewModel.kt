package com.example.vashopify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.data.ProductResponse
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.shop.ProductRepository
import com.example.vashopify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val pagingInfo = PagingInfo1()
    private val _productState =
        MutableStateFlow<ProductEvent>(ProductEvent.Empty)
    val productState: StateFlow<ProductEvent> get() = _productState
    fun fetchProducts(query: String) {
//        _showLoading.value = true
        if(!pagingInfo.isPagingEnd){

            viewModelScope.launch {
                _productState.value = ProductEvent.Loading

                when (val catResponse = productRepository.getSearchedProducts(
                    query = query,
                    page = pagingInfo.page,
                    pageSize = pagingInfo.page * 10
                )) {
                    is Resource.Error -> {
                        Log.e("error", "Resource Error Search")
                        viewModelScope.launch {

                            _productState.value = ProductEvent.Failure
                        }
                    }

                    is Resource.Success -> {
                        val list = catResponse.data!!
                        Log.d("error", "Resource Success Search")
//                    Log.d("error",list.toString())
                        pagingInfo.isPagingEnd = list == pagingInfo.oldSearchProducts
                        pagingInfo.oldSearchProducts = list
                        viewModelScope.launch {

                            _productState.value = ProductEvent.Success(list)
                        }
                        pagingInfo.page++
                    }

                    else -> Unit
                }
            }
        }
    }

    internal class PagingInfo1 {
        var page: Long = 1
        var oldSearchProducts:List<ProductResponse> = emptyList()
        var isPagingEnd :Boolean =false
    }
}