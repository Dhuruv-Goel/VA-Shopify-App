package com.example.vashopify.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
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
import com.example.vashopify.activities.ShoppingActivity
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.auth.ui.AuthUiEvent
import com.example.vashopify.auth.ui.LoginUiState
import com.example.vashopify.auth.ui.RegisterUiState
import com.example.vashopify.databinding.FragmentLoginBinding
import com.example.vashopify.util.LoginValidation
import com.example.vashopify.util.RegisterValidation
import com.example.vashopify.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
//    private val viewModel by viewModels<LoginViewModel>()

    private val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        binding.apply {

            edEmailLogin.addTextChangedListener { text ->
                viewModel.onEvent(AuthUiEvent.LoginEmailChanged(text.toString()))
            }
            edPasswordLogin.addTextChangedListener { text ->
                viewModel.onEvent(AuthUiEvent.LoginPasswordChanged(text.toString()))
            }
            buttonLoginLogin.setOnClickListener {
                viewModel.onEvent(AuthUiEvent.Login)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginUiState.collect { state ->
                    if (state.isLoading) {
                        binding.buttonLoginLogin.startAnimation()
                    }
                }
            }

        }
//        binding.tvForgotPasswordLogin.setOnClickListener {
//            setupBottomSheetDialog { email ->
//                viewModel.resetPassword(email)
//            }
//        }


//        lifecycleScope.launchWhenStarted {
//            viewModel.resetPassword.collect{
//                when (it) {
//                    is Resource.Loading -> {
//                    }
//                    is Resource.Success -> {
//                        Snackbar.make(requireView(),"Reset link was sent to your email",Snackbar.LENGTH_LONG).show()
//                    }
//                    is Resource.Error -> {
//                        Snackbar.make(requireView(),"Error: ${it.message}",Snackbar.LENGTH_LONG).show()
//                    }
//                    else -> Unit
//
//                }
//            }
//        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authResultslogin.collect { result ->
                when (result) {
                    is AuthResult.Authorized -> {
                        binding.buttonLoginLogin.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    is AuthResult.Unauthorized -> {
                        binding.buttonLoginLogin.revertAnimation()
                        showToast("You're not authorized\nPlease fill correct details")
                    }

                    is AuthResult.UnknownError -> {
                        binding.buttonLoginLogin.revertAnimation()
                        showToast("An unknown error occurred login")
                    }
                }

            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validationL.collect { validation ->
                    if (validation.email is LoginValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.edEmailLogin.apply {
                                requestFocus()
                                error = validation.email.message
                            }
                        }
                    }

                    if (validation.password is LoginValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.edPasswordLogin.apply {
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

}