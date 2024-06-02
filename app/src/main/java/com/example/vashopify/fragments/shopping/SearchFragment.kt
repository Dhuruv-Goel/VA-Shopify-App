package com.example.vashopify.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.vashopify.R
import com.example.vashopify.databinding.FragmentSearchBinding
import com.example.vashopify.fragments.shopping.adapters.SearchAdapter
import com.example.vashopify.viewmodel.SearchViewModel


class SearchFragment : Fragment(R.layout.activity_search) {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
 private  lateinit var query :String
    //    private  lateinit var query : String
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.btnSearchBack.setOnClickListener {
//            val intent = Intent(requireContext(), ShoppingActivity::class.java)
//            startActivity(intent)
//        }
//        setupSearchRv()
//        observeProducts()
////         query: String
//        binding.searchBar.setOnSearchActionListener(object : OnSearchActionListener {
//            override fun onSearchStateChanged(enabled: Boolean) {
//
//            }
//
//            override fun onSearchConfirmed(text: CharSequence) {
//                query = text.toString()
//                viewModel.fetchProducts(query)
//            }
//
//            override fun onButtonClicked(buttonCode: Int) {}
//        })
//   binding.swipeSearch.setOnRefreshListener {
//       observeProducts()
//       viewModel.fetchProducts(query)
//       binding.swipeSearch.isRefreshing =false
//   }
//
//        binding.nestedScrollSearch.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v, _, scrollY, _, _->
//            if(v.getChildAt(0).bottom <= v.height +scrollY){
//                observeProducts()
//                viewModel.fetchProducts(query)
//            }
//
//        })
    }

//    private fun setupSearchRv() {
//        searchAdapter = SearchAdapter(requireContext(), fragmentManager = requireActivity().supportFragmentManager)
//        binding.rvSearch.apply {
//            layoutManager =
//                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
//            adapter = searchAdapter
//        }
//    }
//
//    private fun observeProducts() {
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.productState.collect { value ->
//                    when (value) {
//                        is ProductEvent.Success -> {
//                            hideLoading()
//                            val prodList = value.list
////                            showToast(prodList.toString())
//                            searchAdapter.setData(prodList)
//                        }
//
//                        is ProductEvent.Failure -> {
//                            hideLoading()
//                            showToast("Failed To load data1")
//                        }
//
//                        is ProductEvent.Loading -> {
//                            showLoading()
//                        }
//
//                        else -> Unit
//                    }
//                }
//            }
//        }
//    }
//
//    private fun hideLoading() {
//        binding.searchProgressBar.visibility = View.GONE
//    }
//
//    private fun showLoading() {
//        binding.searchProgressBar.visibility = View.VISIBLE
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d("error","pause")
//        query = null.toString()
//    }
//
////    override fun onDestroy() {
////        super.onDestroy()
////        Log.d("error","destroy")
////        query = null.toString()
////    }
}