package com.example.vashopify.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.data.AddressRequest
import com.example.vashopify.data.AddressResponse
import com.example.vashopify.data.AddressUIEvent
import com.example.vashopify.shop.AddressRepository
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressrepository: AddressRepository
) : ViewModel() {

    private val _addressUIstate = MutableStateFlow(AddressRequest())
    val addressUIstate: StateFlow<AddressRequest> get() = _addressUIstate
    private val _getaddress = MutableStateFlow<ProductEvent>(ProductEvent.Empty)
    val getaddress: StateFlow<ProductEvent> get() = _getaddress
    private val resultChannel = Channel<AuthResult<Unit>>()
    val addressResult = resultChannel.receiveAsFlow()
    private val resultChannelupdate = Channel<AuthResult<Unit>>()
    val addressResultupdate = resultChannelupdate.receiveAsFlow()
    private val resultChanneldlt = Channel<AuthResult<Unit>>()
    val addressResultdlt = resultChanneldlt.receiveAsFlow()

    //    private val resultChannelget = Channel<Resource<AddressResponse>>()
//    val addressResultget = resultChannelget.receiveAsFlow()
    private val _showLoading = MutableStateFlow<Boolean>(false)
    val showLoading: StateFlow<Boolean> get() = _showLoading.asStateFlow()


    fun onEvent(event: AddressUIEvent) {
        when (event) {
            is AddressUIEvent.AddressTitleChanged -> {
                _addressUIstate.update { it.copy(addressTitle = event.value) }
            }

            is AddressUIEvent.ShopNameTitleChanged -> {
                _addressUIstate.update { it.copy(shopName = event.value) }

            }

            is AddressUIEvent.StreetChanged -> {
                _addressUIstate.update { it.copy(street = event.value) }

            }

            is AddressUIEvent.PhoneChanged -> {
                _addressUIstate.update { it.copy(phone = event.value) }

            }

            is AddressUIEvent.CityChanged -> {
                _addressUIstate.update { it.copy(city = event.value) }

            }

            is AddressUIEvent.StateChanged -> {
                _addressUIstate.update { it.copy(state = event.value) }

            }
            is AddressUIEvent.Save -> {
                addNewAddress()
            }

            is AddressUIEvent.Update -> {
                updateAddress()
            }

            is AddressUIEvent.Delete -> {
                deleteAddress()
            }
        }
    }


    private fun addNewAddress() {
        _showLoading.value = true
        viewModelScope.launch {
            try {
                val result = addressrepository.createAddress(
                    addressTitle = addressUIstate.value.addressTitle,
                    shopName = addressUIstate.value.shopName,
                    street = addressUIstate.value.street,
                    phone = addressUIstate.value.phone,
                    city = addressUIstate.value.city,
                    state = addressUIstate.value.state
                )
                resultChannel.send(result)

            } catch (e: Exception) {
                Log.e("save add error", e.message.toString() + " Create Address")
            } finally {
                resultChannel.send(AuthResult.UnknownError())
            }
        }
    }

    private fun updateAddress() {
        _showLoading.value = true
        viewModelScope.launch {
            try {
                val result = addressrepository.updateAddress(
                    addressTitle = addressUIstate.value.addressTitle,
                    shopName = addressUIstate.value.shopName,
                    street = addressUIstate.value.street,
                    phone = addressUIstate.value.phone,
                    city = addressUIstate.value.city,
                    state = addressUIstate.value.state
                )
                resultChannelupdate.send(result)
            } catch (e: Exception) {
                Log.e("error", e.message.toString() + " Update Address")
                resultChannelupdate.send(AuthResult.UnknownError())

            }
        }
    }

    fun deleteAddress() {
        _showLoading.value = true
        viewModelScope.launch {
            val result = addressrepository.deleteAddress()
            resultChanneldlt.send(result)
        }
    }

    fun getAddress() {
        _showLoading.value = true
        viewModelScope.launch {
           try {
                when (val result = addressrepository.getAddress()) {
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
               _getaddress.value = ProductEvent.Failure

           }
        }
    }

}