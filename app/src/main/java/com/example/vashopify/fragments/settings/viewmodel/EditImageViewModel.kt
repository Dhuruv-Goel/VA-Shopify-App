package com.example.vashopify.fragments.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.auth.AuthRepository
import com.example.vashopify.auth.AuthResult
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
class EditImageViewModel @Inject constructor(
    private val authRepository: AuthRepository
) :ViewModel() {

    private val resultChannalEdit = Channel<AuthResult<Unit>>()
    val editResult = resultChannalEdit.receiveAsFlow()
        private val _showLoading = MutableStateFlow<Boolean>(false)
    val showLoadingEdit: StateFlow<Boolean> get() = _showLoading.asStateFlow()


    fun uploadImg(part: MultipartBody.Part) {
        viewModelScope.launch {
            _showLoading.value = true

            try {
                val result = authRepository.uploadImage(part)
                resultChannalEdit.send(result)
            } catch (e: Exception) {
                _showLoading.value = false
                // Handle error (e.g., show an error message)
            } finally {
                _showLoading.value = false
            }
        }
    }
}