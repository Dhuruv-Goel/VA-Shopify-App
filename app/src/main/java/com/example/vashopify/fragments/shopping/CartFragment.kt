package com.example.vashopify.fragments.shopping

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vashopify.R
import com.example.vashopify.activities.ShoppingActivity
import com.example.vashopify.data.CartResponse
import com.example.vashopify.databinding.FragmentCartBinding
import com.example.vashopify.fragments.shopping.adapters.CartAdapter
import com.example.vashopify.fragments.shopping.viewmodel.CartViewModel
import com.example.vashopify.util.VertcalItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private val viewModel: CartViewModel by viewModels()
    private lateinit var cart: CartResponse
    private lateinit var sharedPreferences: SharedPreferences
    private var totalPrice by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater)
        sharedPreferences = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCartRv()
        observeCart()
        setupListeners()
        getTotal()
        viewModel.getCart()
    }

    private fun setupCartRv() {
        cartAdapter = CartAdapter(
            requireContext(),
            onPlusClick = { position -> viewModel.increase(position) },
            onMinusClick = { position -> viewModel.decrease(position) },
            onLongPress = { position -> showDeleteConfirmationDialog(position) },
            fragmentManager = requireActivity().supportFragmentManager
        )

        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
            addItemDecoration(VertcalItemDecoration())
        }
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Delete item from cart")
            setMessage("Do you want to delete this item from your cart?")
            setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            setPositiveButton("Yes") { dialog, _ ->
                viewModel.delete(position)
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun observeCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartItems.collectLatest { list ->
                    if (list.items.isEmpty()) {
                        showEmptyCart()
                    } else {
                        cart = list
                        hideEmptyCart()
                        cartAdapter.setData(list.items)
                    }
                    (activity as? ShoppingActivity)?.forCounterChanged()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getTotal() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productsPrice.collectLatest { price ->
                binding.tvTotalPrice.text = "₹ $price"
                totalPrice = price
                sharedPreferences.edit().putInt("tp", price).apply()
            }
        }
    }

    private fun setupListeners() {
        binding.buttonCheckout.setOnClickListener {
            viewModel.updateTotalPrice(totalPrice)
            val action = CartFragmentDirections.actionCartFragment2ToBillingFragment(cart)
            findNavController().navigate(action)
        }

        binding.btnCloseCart.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment2_to_homeFragment2)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.showLoading.observe(viewLifecycleOwner) { showLoading ->
                if (showLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        }
    }

    private fun showLoading() {
        binding.cartProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.cartProgressBar.visibility = View.GONE
    }

    private fun showEmptyCart() {
        binding.layoutCartEmpty.visibility = View.VISIBLE
        hideOtherViews()
    }

    private fun hideEmptyCart() {
        binding.layoutCartEmpty.visibility = View.GONE
        showOtherViews()
    }

    private fun showOtherViews() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideOtherViews() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? ShoppingActivity)?.forCounterChanged()
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav?.visibility = View.VISIBLE
    }
}
