package com.example.vashopify.fragments.settings.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.CloudinaryConfig
import com.example.vashopify.auth.AuthRepository
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.data.User
import com.example.vashopify.fragments.settings.data.ui.AccountUiEvent
import com.example.vashopify.fragments.settings.data.ui.AccountUiState
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.util.RegisterValidation
import com.example.vashopify.util.Resource
import com.example.vashopify.util.validateEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    app: Application
) : AndroidViewModel(app) {

    private val _user = MutableStateFlow<ProductEvent>(ProductEvent.Empty)
    val user: StateFlow<ProductEvent> get() = _user.asStateFlow()
//    private val _editInfo = MutableStateFlow<ProductEvent>(ProductEvent.Empty)
//    val editInfo: StateFlow<ProductEvent> get() = _editInfo

    private val resultChannelAccount = Channel<AuthResult<Unit>>()
    val accountResult = resultChannelAccount.receiveAsFlow()
    private val _accountUiState = MutableStateFlow(AccountUiState())
    val accountUiState: StateFlow<AccountUiState> get() = _accountUiState

    //    private val _showLoading = MutableStateFlow<Boolean>(false)
//    val showLoadingAcc: StateFlow<Boolean> get() = _showLoading.asStateFlow()
    init {
        getAccount()
    }

    fun onEvent(event: AccountUiEvent) {
        when (event) {
            is AccountUiEvent.FirstNameChanged -> {
                _accountUiState.update { it.copy(firstName = event.value) }
            }

            is AccountUiEvent.LastNameChanged -> {
                _accountUiState.update { it.copy(lastName = event.value) }
            }

            is AccountUiEvent.EmailChanged -> {
                _accountUiState.update { it.copy(email = event.value) }
            }
//            is AccountUiEvent.ProfileImageChanged -> {
//                _accountUiState.update { it. copy(profileImageUrl = event.value) }
//            }

            is AccountUiEvent.Update -> {
                updateUser()
            }
        }
    }

    fun getAccount() {
        viewModelScope.launch {
            _user.value = ProductEvent.Loading

            try {
                when (val result = authRepository.getAccount()) {
                    is Resource.Success -> {
                        viewModelScope.launch {
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



        fun updateUser() {
            val firstName = accountUiState.value.firstName
            val lastName = accountUiState.value.lastName
            val email = accountUiState.value.email
//        val profileImageUrl = accountUiState.value.profileImageUrl
            viewModelScope.launch {
                _accountUiState.update { it.copy(isLoading = true) }

            }
            val areInputValid = validateEmail(email) is RegisterValidation.Success
                    && firstName.trim().isNotEmpty()
                    && lastName.trim().isNotEmpty()

            if (!areInputValid) {
                viewModelScope.launch {
                    _accountUiState.update {
                        it.copy(isError = true)
                    }
                    _accountUiState.update { it.copy(isLoading = false) }
                }
                return
            }

            viewModelScope.launch {
                try {
                    val accResult = authRepository.updateAccount(
                        user = User(
                            firstName = firstName,
                            lastName = lastName,
                            email = email
                        )
                    )
                    resultChannelAccount.send(accResult)
                } catch (e: Exception) {
                    // Handle error (e.g., show an error message)
                } finally {
                    _accountUiState.update { it.copy(isLoading = false) }
                }
            }
        }

//    private fun savedUserInformationWithNewImage(user: User, imageUri: Uri) {
//      viewModelScope.launch {
//          try {
//              val imageBitmap = MediaStore.Images.Media.getBitmap(
//                  getApplication<VAShopifyApplication>().contentResolver,imageUri)
//
//              val byteArrayOutputStream = ByteArrayOutputStream()
//              imageBitmap.compress(Bitmap.CompressFormat.JPEG,96,byteArrayOutputStream)
//              val imageByteArray = byteArrayOutputStream.toByteArray()
//              val imageDirectory =
//          }catch(e:Exception){
//
//          }
//      }
//    }


    }