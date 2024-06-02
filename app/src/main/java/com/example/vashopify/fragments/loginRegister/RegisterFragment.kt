package com.example.vashopify.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.vashopify.R
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.auth.ui.AuthUiEvent
import com.example.vashopify.auth.ui.RegisterUiState
import com.example.vashopify.databinding.FragmentRegisterBinding
import com.example.vashopify.util.RegisterValidation
import com.example.vashopify.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//private val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
//    private val viewModel by viewModels<RegisterViewModel>()
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDoYouHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.registerUiState.collect{state->
//                updateUi(state)
//            }
//        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerUiState.collect {state->
                    if (state.isLoading) {
                        binding.buttonRegisterRegister.startAnimation()
                    }
                }
            }

        }
        binding.apply {

            edFirstNameRegister.addTextChangedListener { text ->
                viewModel.onEvent(AuthUiEvent.RegisterFirstNameChanged(text.toString()))
            }
            edLastNameRegister.addTextChangedListener { text ->
                viewModel.onEvent(AuthUiEvent.RegisterLastNameChanged(text.toString()))
            }
            edMobileRegister.addTextChangedListener { text ->
                viewModel.onEvent(AuthUiEvent.RegisterMobileChanged(text.toString()))
            }
            edEmailRegister.addTextChangedListener { text ->
                viewModel.onEvent(AuthUiEvent.RegisterEmailChanged(text.toString()))
            }
            edPasswordRegister.addTextChangedListener { text ->
                viewModel.onEvent(AuthUiEvent.RegisterPasswordChanged(text.toString()))
            }
            buttonRegisterRegister.setOnClickListener {
                viewModel.onEvent(AuthUiEvent.Register)
            }
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authResultsRegister.collect { result ->
                when (result) {
                    is AuthResult.Authorized -> {
                        binding.buttonRegisterRegister.revertAnimation()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

                    }

                    is AuthResult.Unauthorized -> {
                        binding.buttonRegisterRegister.revertAnimation()
//                        Log.e("error","unauthorised")
                        showToast("You're not authorized")
                    }

                    is AuthResult.UnknownError -> {
                        binding.buttonRegisterRegister.revertAnimation()
                        Log.e("error", "uncommon error")
                        showToast("An unknown error occurred..........")
                    }
                }

            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validationR.collect { validation ->
                    if (validation.email is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.edEmailRegister.apply {
                                requestFocus()
                                error = validation.email.message
                            }
                        }
                    }

                    if (validation.password is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.edPasswordRegister.apply {
                                requestFocus()
                                error = validation.password.message
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
//    private fun updateUi(state:RegisterUiState){
//        binding.apply {
//            edFirstNameRegister.setText(state.registerFirstName)
//            edLastNameRegister.setText(state.registerLastName)
//            edMobileRegister.setText(state.registerMobile)
//            edEmailRegister.setText(state.registerEmail)
//            edPasswordRegister.setText(state.registerPassword)
//
//        }
//    }
}


//viewModel.register.collect { it->
//    when (it) {
//        is Resource.Loading -> {
//            binding.buttonRegisterRegister.startAnimation()
//        }
//        is Resource.Success -> {
//            binding.buttonRegisterRegister.revertAnimation()
//        }
//        is Resource.Error -> {
//            Log.e(TAG,it.message.toString())
//            binding.buttonRegisterRegister.revertAnimation()
//        }
//        else -> Unit
//    }
//}