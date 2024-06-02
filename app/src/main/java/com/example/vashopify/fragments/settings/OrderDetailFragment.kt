package com.example.vashopify.fragments.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vashopify.R
import com.example.vashopify.data.OrderStatus
import com.example.vashopify.data.getOrderStatus
import com.example.vashopify.databinding.FragmentOrderDetailBinding
import com.example.vashopify.fragments.shopping.adapters.BillingProductAdapter
import com.example.vashopify.util.VertcalItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
class OrderDetailFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetailBinding
    private lateinit var billingProductAdapter: BillingProductAdapter
    private val args by navArgs<OrderDetailFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackButtonPressed()
        }
        setupProductRv()
        val order = args.orderDetails
        binding.apply {
            imageCloseOrder.setOnClickListener {
                findNavController().navigate(R.id.action_orderDetailFragment_to_allOrdersFragment)
            }
            tvOrderId.text = "Order #${order.orderId}"
            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shipped.status,
                    OrderStatus.Delivered.status

                )
            )

            val currentOrderStatus = when (getOrderStatus(order.orderStatus)) {
                is OrderStatus.Ordered -> 0
                is OrderStatus.Confirmed -> 1
                is OrderStatus.Shipped -> 2
                is OrderStatus.Delivered -> 3
                else -> 0
            }

            stepView.go(currentOrderStatus, false)
            if (currentOrderStatus == 3) {
                stepView.done(true)
            }

            tvFullName.text = order.address.shopName
            tvAddress.text = "${order.address.street},${order.address.city}"
            tvPhoneNumber.text = order.address.phone
            tvTotalPrice.text = "₹ ${order.totalPrice}"

            billingProductAdapter.setData(order.products)
        }
    }

    private fun onBackButtonPressed() {
        findNavController().navigate(R.id.action_orderDetailFragment_to_allOrdersFragment)
    }


    private fun setupProductRv() {
        billingProductAdapter = BillingProductAdapter(requireContext())
        binding.rvProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = billingProductAdapter
            addItemDecoration(VertcalItemDecoration())
        }
    }
}