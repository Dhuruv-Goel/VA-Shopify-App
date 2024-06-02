package com.example.vashopify.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vashopify.R
import com.example.vashopify.databinding.FragmentOrdersBinding
import com.example.vashopify.fragments.settings.viewmodel.OrderViewModel
import com.example.vashopify.shop.ProductEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllOrdersFragment : Fragment(R.layout.fragment_orders) {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var orderAdapter: OrdersAdapter
    private val viewmodel: OrderViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackButtonPressed()
        }
        binding.imageCloseOrders.setOnClickListener {
            findNavController().navigate(R.id.action_allOrdersFragment_to_profileFragment2)
        }
        setupOrderRv()
        observeOrders()
        viewmodel.getAllOrders()
    }

    private fun onBackButtonPressed() {
        findNavController().navigate(R.id.action_allOrdersFragment_to_profileFragment2)

    }

    private fun setupOrderRv() {
        orderAdapter = OrdersAdapter(
            requireContext(),
           onItemClickListener = {
               val action = AllOrdersFragmentDirections.actionAllOrdersFragmentToOrderDetailFragment(it)
               findNavController().navigate(action)
           }
        )
        binding.rvAllOrders.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = orderAdapter
        }
    }

    private fun observeOrders() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.order.collectLatest { result ->
                    when (result) {
                        is ProductEvent.OrderSuccess -> {
                            val list = result.orders
                            hideLoadingRv()
                            orderAdapter.setData(list)

                            if (list.isNullOrEmpty()) {
                                binding.tvEmptyOrders.visibility = View.VISIBLE
                            }
                        }

                        is ProductEvent.Unauthorised -> {
                            hideLoadingRv()
                            showToast("Unauthorized")
                        }

                        is ProductEvent.Failure -> {
                            hideLoadingRv()
                            showToast("Failure")
                        }

                        is ProductEvent.Loading -> {
                            showLoadingRv()
                        }

                        else -> Unit
                    }
                }
            }
//
        }


    }

    private fun hideLoadingRv() {
        binding.progressbarAllOrders.visibility = View.GONE
    }

    private fun showLoadingRv() {
        binding.progressbarAllOrders.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }



    override fun onPause() {
        super.onPause()
//        showToast("All Orders Pause")

    }

}
