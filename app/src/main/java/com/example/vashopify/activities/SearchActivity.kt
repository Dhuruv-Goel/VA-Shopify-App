package com.example.vashopify.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vashopify.databinding.ActivitySearchBinding
import com.example.vashopify.fragments.shopping.adapters.SearchAdapter
import com.example.vashopify.viewmodel.SearchViewModel
import com.example.vashopify.shop.ProductEvent
import com.mancj.materialsearchbar.MaterialSearchBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity :AppCompatActivity(){
  private val binding by lazy {
      ActivitySearchBinding.inflate(layoutInflater)
  }
    private lateinit var searchAdapter: SearchAdapter
    private  lateinit var query :String
    private val viewModel: SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnSearchBack.setOnClickListener {
            val intent = Intent(this, ShoppingActivity::class.java)
            startActivity(intent)
            finish()
        }
        setupSearchRv()
        observeProducts()
        binding.searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {

            }

            override fun onSearchConfirmed(text: CharSequence) {
                query = text.toString()
                viewModel.fetchProducts(query)
            }

            override fun onButtonClicked(buttonCode: Int) {}
        })
        binding.swipeSearch.setOnRefreshListener {
            observeProducts()
            viewModel.fetchProducts(query)
            binding.swipeSearch.isRefreshing =false
        }

        binding.nestedScrollSearch.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v, _, scrollY, _, _->
            if(v.getChildAt(0).bottom <= v.height +scrollY){
                observeProducts()
                viewModel.fetchProducts(query)
            }

        })
    }

    private fun setupSearchRv() {
        searchAdapter = SearchAdapter(this@SearchActivity, fragmentManager = this.supportFragmentManager)
        binding.rvSearch.apply {
            layoutManager =
                GridLayoutManager(this@SearchActivity, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchAdapter
        }
    }

    private fun observeProducts() {

       lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productState.collect { value ->
                    when (value) {
                        is ProductEvent.Success -> {
                            hideLoading()
                            val prodList = value.list
                            searchAdapter.setData(prodList)
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
        binding.searchProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.searchProgressBar.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this@SearchActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        Log.d("error","pause")
        query = null.toString()
    }

}