package com.example.vashopify.fragments.shopping

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vashopify.R
import com.example.vashopify.activities.ShoppingActivity
import com.example.vashopify.data.AddressRequest
import com.example.vashopify.data.CartItem
import com.example.vashopify.data.CartItemResponse
import com.example.vashopify.data.OrderRequest
import com.example.vashopify.data.OrderStatus
import com.example.vashopify.databinding.FragmentBillingBinding
import com.example.vashopify.fragments.settings.OrderDetailFragmentArgs
import com.example.vashopify.fragments.shopping.adapters.BillingProductAdapter
import com.example.vashopify.fragments.shopping.viewmodel.BillingViewModel
import com.example.vashopify.fragments.shopping.viewmodel.CartViewModel
import com.example.vashopify.fragments.settings.viewmodel.OrderViewModel
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.util.VertcalItemDecoration
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random
import kotlin.properties.Delegates

@AndroidEntryPoint
class BillingFragment : Fragment(R.layout.fragment_billing) {
    private lateinit var binding: FragmentBillingBinding
    private lateinit var billingAdapter: BillingProductAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var isSaved: String
    private lateinit var finalAddress: AddressRequest
    private var totalPrice by Delegates.notNull<Int>()
    private lateinit var cartItems: List<CartItemResponse>
    private val billingViewModel: BillingViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val billargs by navArgs<BillingFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackButtonPressed()
        }
        finalAddress = AddressRequest()
        sharedPreferences = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE)
        isSaved = sharedPreferences.getString("isSaved", "false").toString()
        totalPrice = sharedPreferences.getInt("tp", 0)
        binding.tvTotalPrice.text = "₹ $totalPrice"
        showToast(isSaved)
        if (isSaved == "true") {
            billingViewModel.getAddress()
        }
        if (isSaved == "false") {
            binding.tvSelectAddress.visibility = View.VISIBLE
            binding.layoutAddress.visibility = View.GONE
        }
        setupCartRv()
        collectAddress()
//        getTotal()
//        observeCart()
        observeOrder()
       cartItems = billargs.cartItems.items
        billingAdapter.setData(cartItems)
        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_cartFragment2)
        }
        binding.btnAddAddress.setOnClickListener {
//            val intent = Intent(requireContext(), AddressActivity::class.java)
//            startActivity(intent)
            findNavController().navigate(R.id.action_billingFragment_to_addressActivity)
        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            billingViewModel.showLoading.observe(viewLifecycleOwner) { showLoading ->
//                if (showLoading) {
//                    showLoadingRv()
//                } else {
//                    hideLoadingRv()
//                }
//            }
//
//        }
        viewLifecycleOwner.lifecycleScope.launch {
            billingViewModel.showLoadingAdrs.observe(viewLifecycleOwner) { showLoading ->
                if (showLoading) {
                    showLoadingAddress()
                } else {
                    hideLoadingAddress()
                }
            }

        }

        binding.buttonPlaceOrder.setOnClickListener {
            if (isSaved == "false") {
                showToast("Please save the address")
                return@setOnClickListener
            }
            showOrderConfirmationDialog()
        }
//        viewModel.getAddress()        billingViewModel.getCart()

    }
    // Function to copy specific data from CartItemResponse to CopiedData
    fun copySpecificData(cartItem: CartItemResponse): CartItem {
        // Extract specific data from the CartItemResponse
        val productId = cartItem.productId._id
        val quantity = cartItem.quantity

        // Create a CopiedData object with the extracted data
        return CartItem(productId, quantity)
    }

    // Function to copy specific data from a list of CartItemResponse to another list of CopiedData
    fun copySpecificDataList(cartItemList: List<CartItemResponse>): List<CartItem> {
        // Define a list to store the copied data
        val copiedList: MutableList<CartItem> = mutableListOf()

        // Iterate over the list of CartItemResponse
        for (cartItem in cartItemList) {
            // Copy specific data from each CartItemResponse
            val copiedData = copySpecificData(cartItem)

            // Add the copied data to the list
            copiedList.add(copiedData)
        }

        // Return the list containing the copied data
        return copiedList
    }

    private fun generateOrderID(): String {
        val timestamp = System.currentTimeMillis()
        val random = Random().nextInt(10000) // Adjust the range based on your requirements

        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val formattedDate = sdf.format(Date(timestamp))
        Log.d("order", "Date: $formattedDate")

        return "$formattedDate$random"
    }
    private fun showOrderConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Place Order")
            setMessage("Do you want to order your cart items?")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { dialog, _ ->
                val order = OrderRequest(
                    orderStatus = OrderStatus.Ordered.status,
                    totalPrice = totalPrice,
                    products = copySpecificDataList(cartItems),
                    address = finalAddress,
                    orderId = generateOrderID()
                )
                Log.d("order", order.toString())
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }

        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun onBackButtonPressed() {
        // Get the FragmentManager
//        val fragmentManager = requireActivity().supportFragmentManager
//
//        // Remove the current fragment
//        fragmentManager.beginTransaction().remove(this).commit()
  findNavController().navigate(R.id.action_billingFragment_to_cartFragment2)
        cartViewModel.getCart()
        // Or use popBackStack if the fragment is added to the back stack
        // fragmentManager.popBackStack()// Remove the fragment from the back stack
    }

    private fun setupCartRv() {

        billingAdapter = BillingProductAdapter(
            requireContext()
        )
        binding.rvProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = billingAdapter
            addItemDecoration(VertcalItemDecoration())
        }
//        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
//            cartAdapter.setData(cartItems)
//        }
    }


    private fun observeOrder() {
        viewLifecycleOwner.lifecycleScope.launch {
            orderViewModel.order.collect { result ->
                when (result) {
                    is ProductEvent.UnitSuccess -> {
                        binding.buttonPlaceOrder.revertAnimation()
//                        onBackButtonPressed()
//                        (activity as? ShoppingActivity)?.forCounterChanged()
                        findNavController().navigate(R.id.action_billingFragment_to_cartFragment2)
//                        cartViewModel.getCart()

                        Snackbar.make(requireView(), "Your Order was placed", Snackbar.LENGTH_LONG)
                            .show()
//                        sharedPreferences.edit()
//                            .putString("NoCart","true")
//                            .apply()
                    }

                    is ProductEvent.Unauthorised -> {
                        binding.buttonPlaceOrder.revertAnimation()
                    }

                    is ProductEvent.Failure -> {
                        binding.buttonPlaceOrder.revertAnimation()
                       showToast("Place Failure")
                    }

                    is ProductEvent.Loading -> {
                        binding.buttonPlaceOrder.startAnimation()

                    }

                    else -> Unit
                }
            }
        }
    }

//    @SuppressLint("SetTextI18n")
//    fun getTotal() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            billingViewModel.totalPriceState.collectLatest { price ->
//                price.let {
//
//                    totalPrice = price
//                }
//
//            }
//        }
//    }

//    private fun observeCart() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                billingViewModel.cartItems.observe(viewLifecycleOwner) { list ->
//                    products = list
//                    billingAdapter.setData(list)
//
//                }
//
//            }
//        }
//
//    }

    @SuppressLint("SetTextI18n")
    private fun collectAddress() {
        lifecycleScope.launch {
            billingViewModel.getaddress.collect { response ->
                when (response) {
                    is ProductEvent.AddressSuccess -> {
                        val addres = response.address
                        Log.d("get1", addres.toString())
                        hideLoadingAddress()

                        binding.tvSelectAddress.visibility = View.GONE
                        binding.layoutAddress.visibility = View.VISIBLE
                        binding.layoutTvAddress
                        binding.layoutTvAddress.tvAddressTitle.text = addres.addressTitle
                        binding.layoutTvAddress.tvShopName.text = addres.shopName
                        binding.layoutTvAddress.tvStreet.text = addres.street
                        binding.layoutTvAddress.tvCityState.text =
                            "${addres.city},${addres.state}"
                        finalAddress.addressTitle = addres.addressTitle
                        finalAddress.shopName = addres.shopName
                        finalAddress.street = addres.street
                        finalAddress.phone = addres.phone
                        finalAddress.city = addres.city
                        finalAddress.state = addres.state


//                           sharedPreferences.edit()
//                               .put
//                               .apply()

                    }

                    is ProductEvent.Failure -> {
//                        showToast("Data Loading error")
                        binding.tvSelectAddress.visibility = View.VISIBLE
                        binding.layoutAddress.visibility = View.GONE
                        hideLoadingAddress()
                    }

                    is ProductEvent.Unauthorised -> {
                        hideLoadingAddress()
                    }

                    else -> Unit
                }


            }

        }
    }

    private fun hideLoadingAddress() {
        binding.progressbarAddress.visibility = View.GONE
//        binding.tvSelectAddress.visibility=View.GONE
    }

    private fun showLoadingAddress() {
        binding.progressbarAddress.visibility = View.VISIBLE
    }

    private fun hideLoadingRv() {
        binding.rvProgressBar.visibility = View.GONE
    }

    private fun showLoadingRv() {
        binding.rvProgressBar.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
//        showToast("resume billing")
        billingViewModel.getAddress()
        billingViewModel.getCart()
    }

}