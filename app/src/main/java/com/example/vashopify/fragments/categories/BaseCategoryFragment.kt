package com.example.vashopify.fragments.categories

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.vashopify.R
import com.example.vashopify.activities.ShoppingActivity
import com.example.vashopify.adapters.BaseFragmentAdapter
import com.example.vashopify.databinding.FragmentBaseCategoryBinding
import com.example.vashopify.fragments.categories.viewModel.BaseCatgoryViewModel
import com.example.vashopify.shop.ProductEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BaseCategoryFragment : Fragment(R.layout.fragment_base_category),SwipeRefreshLayout.OnRefreshListener{
    private lateinit var binding: FragmentBaseCategoryBinding
    private lateinit var baseAdapter: BaseFragmentAdapter
    private val viewModel: BaseCatgoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnBaseBack.setOnClickListener {
            val intent = Intent(requireContext(), ShoppingActivity::class.java)
            startActivity(intent)
        }
        setupBaseRv()


        observeProducts()
        val arguments = arguments
        if (arguments != null) {
            val categoryName = arguments.getString("categoryName", "")

            binding.tvToolbar.text = categoryName
            viewModel.fetchProducts(categoryName)
            binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{v,_,scrollY,_,_->
                if(v.getChildAt(0).bottom <= v.height +scrollY){
                    observeProducts()
                    viewModel.fetchProducts(categoryName)
                }

            })
        }
      setupSwipeRefreshLayout()





    }


    private fun setupSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener(this)
    }
    override fun onRefresh() {

        observeProducts()
        val arguments = arguments
        if (arguments != null) {
            val categoryName = arguments.getString("categoryName", "")

            binding.tvToolbar.text = categoryName
            viewModel.fetchProducts(categoryName)
        }

        binding.swipeRefresh.isRefreshing = false
    }

    private fun setupBaseRv() {
        baseAdapter = BaseFragmentAdapter(requireContext(), fragmentManager = requireActivity().supportFragmentManager)
        binding.rvBase.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = baseAdapter
        }
    }

    private fun observeProducts() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.baseState.collect { value ->
                    when (value) {
                        is ProductEvent.Success -> {
                            hideLoading()
                            val prodList = value.list
//                            showToast(prodList.toString())
                            baseAdapter.setData(prodList)
                        }

                        is ProductEvent.Failure -> {
                            hideLoading()
                            showToast("Failed To load data1")
                        }

                        is ProductEvent.Loading -> {
                            showLoading()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun hideLoading() {
        binding.productsProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.productsProgressBar.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        (activity as? ShoppingActivity)?.forCounterChanged()
    }

}

//            val actionBar: ActionBar? = (activity as? AppCompatActivity)?.supportActionBar
//
//            actionBar?.apply {
//                title = categoryName
//
//                setDisplayHomeAsUpEnabled(true).toString()
//            }