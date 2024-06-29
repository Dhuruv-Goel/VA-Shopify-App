package com.example.vashopify.fragments.shopping

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.vashopify.R
import com.example.vashopify.activities.ShoppingActivity
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.databinding.FragmentProductDetailsBinding
import com.example.vashopify.fragments.shopping.viewmodel.CartViewModel
import com.example.vashopify.fragments.shopping.viewmodel.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var id: String
    private lateinit var sharedPreferences: SharedPreferences
  private lateinit var noCart :String
        private lateinit var isAdded :String
    private val viewModel: ProductDetailViewModel by viewModels()
    private val cartViewModel : CartViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//         isAdded = ""
        sharedPreferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        noCart = sharedPreferences.getString("NoCart", "false").toString()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackButtonPressed()
        }
        val arguments = arguments
        if (arguments != null) {
            val imagePrd = arguments.getString("images", "")

            binding.tvbrandName.text = arguments.getString("brandName", "")
            binding.tvProductName.text = arguments.getString("name", "")
            binding.tvDescription.text = arguments.getString("description", "")
            binding.tvQuantity.text = arguments.getString("quantity", "")
            binding.tvMrpPrice.text = "₹ ${arguments.getInt("mrpPrice")}"
            binding.tvratePrice.text = "₹ ${arguments.getInt("ratePrice")}"
            id = arguments.getString("id", "")
            isAdded = arguments.getString("isAdded","")
//            viewModel.getProduct(id)
            if (isAdded == "true") {
//
                binding.btnAddToCartDetail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            if(isAdded == "false"){
                binding.btnAddToCartDetail
            }


            Log.d("error", id)
            Glide.with(requireContext())
                .load(imagePrd)
                .into(binding.imgProduct)
        }
//        binding.btnAddToCartDetail.setOnClickListener{
//
//        }
        observeResult()
        binding.btnAddToCartDetail.setOnClickListener {

            viewModel.addProductToCart(id)
            cartViewModel.getCart()
            viewModel.updateAdded(id)
//            Log.d("error", id + "1")
        }
    }

//    override fun onStart() {
//        super.onStart()
//        viewModel.getProduct(id)
//        showToast("Start")
////        viewLifecycleOwner.lifecycleScope.launch {
////            viewModel.isAddedState.observe(viewLifecycleOwner) {
////                if (it) {
////                    binding.btnAddToCartDetail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
////
////                }
////            }
////        }
//
//    }
private fun onBackButtonPressed() {
    val fragmentManager = requireActivity().supportFragmentManager

    fragmentManager.beginTransaction().remove(this).commit()
            (activity as? ShoppingActivity)?.forCounterChanged()
}
    private fun observeResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.showLoading.collect { showLoading ->
                if (showLoading) {
                    binding.btnAddToCartDetail.startAnimation()

                }
            }

        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateResult.collect { state ->
                when (state) {
                    is AuthResult.Authorized -> {
                        binding.btnAddToCartDetail.revertAnimation()
//                        showToast("updated")
//                             binding.btnAddToCartDetail.setBackgroundColor(resources.getColor(R.color.black))
                    }

                    is AuthResult.UnknownError -> {
                        binding.btnAddToCartDetail.revertAnimation()
//                        showToast("Error in update")
                    }

                    else -> Unit
                }
            }

        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addToCartResult.collect { result ->
                when (result) {
                    is AuthResult.Authorized -> {
                        binding.btnAddToCartDetail.revertAnimation()
                        binding.btnAddToCartDetail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
//                        isAdded = "true"
                        showToast("Product has added")
                    }

                    is AuthResult.Unauthorized -> {
                        binding.btnAddToCartDetail.revertAnimation()
                        showToast("Please Login First")
                    }

                    is AuthResult.UnknownError -> {
                        binding.btnAddToCartDetail.revertAnimation()
                        showToast("Unknown error")
                    }
                }

            }
        }


    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}