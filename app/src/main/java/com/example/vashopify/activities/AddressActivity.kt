package com.example.vashopify.activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.data.AddressUIEvent
import com.example.vashopify.databinding.ActivityAddressBinding
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddressBinding.inflate(layoutInflater)
    }
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: AddressViewModel by viewModels()
    private lateinit var isSaved: String


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        collectAddress()
        observeLoading()
        observeDelete()
        observeCreate()
        observeUpdatedAddress()
        binding.btnAddressClose.setOnClickListener {
            finish()
        }

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)


        binding.apply {
            edAddressTitle.addTextChangedListener { text ->
                viewModel.onEvent(AddressUIEvent.AddressTitleChanged(text.toString()))

            }
            edShopName.addTextChangedListener { text ->
                viewModel.onEvent(AddressUIEvent.ShopNameTitleChanged(text.toString()))

            }
            edStreet.addTextChangedListener { text ->
                viewModel.onEvent(AddressUIEvent.StreetChanged(text.toString()))

            }
            edPhone.addTextChangedListener { text ->
                viewModel.onEvent(AddressUIEvent.PhoneChanged(text.toString()))

            }
            edCity.addTextChangedListener { text ->
                viewModel.onEvent(AddressUIEvent.CityChanged(text.toString()))

            }
            edState.addTextChangedListener { text ->
                viewModel.onEvent(AddressUIEvent.StateChanged(text.toString()))

            }
            buttonSave.setOnClickListener {
                viewModel.onEvent(AddressUIEvent.Save)
            }
            buttonUpdate.setOnClickListener {
                viewModel.onEvent(AddressUIEvent.Update)
            }
            buttonDelelte.setOnClickListener {
                viewModel.onEvent(AddressUIEvent.Delete)
            }
        }

        isSaved = sharedPreferences.getString("isSaved", "false").toString()
        showToast(isSaved)
        if (isSaved == "true") {
            viewModel.getAddress()
        }
        if (isSaved == "false") {
            binding.buttonSave.visibility = View.VISIBLE
            binding.buttonUpdate.visibility = View.GONE
            binding.edAddressTitle.requestFocus()
            binding.edAddressTitle.text.clear()
            binding.edShopName.text.clear()
            binding.edStreet.text.clear()
            binding.edPhone.text.clear()
            binding.edCity.text.clear()
            binding.edState.text.clear()
        }
        binding.buttonDelelte.setOnClickListener {
            viewModel.deleteAddress()

        }
    }

    private fun observeLoading() {
        lifecycleScope.launch {
            viewModel.showLoading.collect { showLoading ->
                if (showLoading) {
                    showLoading()
                }
            }
        }
    }

    private fun observeUpdatedAddress() {
        lifecycleScope.launch {

            viewModel.addressResultupdate.collect { response ->
                when (response) {
                    is AuthResult.Authorized -> {
                        showToast("Updated Successfully")
                        updateSharedPreferences("true")
                        hideLoading()
                    }

                    is AuthResult.Unauthorized -> {
                        hideLoading()
                    }

                    is AuthResult.UnknownError -> {
                        hideLoading()
                    }

                }


            }
        }
    }

    private fun collectAddress() {
        lifecycleScope.launch {
            viewModel.getaddress.collect { response ->
                when (response) {
                    is ProductEvent.AddressSuccess -> {
                        val addres = response.address
                        Log.d("get1", addres.toString())
                        binding.edAddressTitle.setText(addres.addressTitle)
                        binding.edShopName.setText(addres.shopName)
                        binding.edStreet.setText(addres.street)
                        binding.edPhone.setText(addres.phone)
                        binding.edState.setText(addres.state)
                        binding.edCity.setText(addres.city)
                        hideLoading()
                    }

                    is ProductEvent.Failure -> {
                        showToast("Data Loading error")
                        hideLoading()
                    }

                    is ProductEvent.Unauthorised -> {
                        hideLoading()
                    }

                    else -> Unit
                }


            }

        }
        binding.buttonSave.visibility = View.GONE
        binding.buttonUpdate.visibility = View.VISIBLE
    }

    private fun observeCreate() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addressResult.collect { response ->
                    when (response) {
                        is AuthResult.Authorized -> {
                            showToast("Saved Successfully")
                            hideLoading()
                            binding.buttonSave.visibility = View.GONE
                            binding.buttonUpdate.visibility = View.VISIBLE
                            updateSharedPreferences("true")
                        }

                        is AuthResult.Unauthorized -> {
                            showToast("Please login First")
                            hideLoading()
                        }

                        is AuthResult.UnknownError -> {
                            showToast("Unknown Error")
                            hideLoading()
                        }
                    }

                }
            }
        }
    }

    private fun observeDelete() {
        lifecycleScope.launch {
            viewModel.addressResultdlt.collectLatest { response ->
                when (response) {
                    is AuthResult.Authorized -> {
                        binding.edAddressTitle.text.clear()
                        binding.edShopName.text.clear()
                        binding.edStreet.text.clear()
                        binding.edPhone.text.clear()
                        binding.edCity.text.clear()
                        binding.edState.text.clear()

                        binding.buttonSave.visibility = View.VISIBLE
                        binding.buttonUpdate.visibility = View.GONE
                        showToast("Deleted Successfully")
                        hideLoading()
                        updateSharedPreferences("false")
                    }

                    is AuthResult.Unauthorized -> {
                        showToast("Please login First")
                        hideLoading()
                    }

                    is AuthResult.UnknownError -> {
                        showToast("Unknown Error")
                        hideLoading()
                    }
                }

            }
        }

    }

    private fun hideLoading() {
        binding.progressbarAddress.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressbarAddress.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this@AddressActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun updateSharedPreferences(value: String) {
        sharedPreferences.edit()
            .putString("isSaved", value)
            .apply()

    }


}
