package com.example.vashopify.fragments.categories

import CategoryEvent
import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.vashopify.R
import com.example.vashopify.activities.ShoppingActivity
import com.example.vashopify.adapters.MainBeautyProductsAdapter
import com.example.vashopify.adapters.MainPersonalProductsAdapter
import com.example.vashopify.adapters.MainWinterWearProductsAdapter
import com.example.vashopify.data.CategoryResponse
import com.example.vashopify.data.ProductResponse
import com.example.vashopify.databinding.FragmentMainCategoryBinding
import com.example.vashopify.fragments.categories.ui.CategoryAdapter
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.viewmodel.MainCategoryViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask
import kotlin.properties.Delegates

private val TAG = "MainCategoryFragment"

@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category),
    SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var mainBeautyProductsAdapter: MainBeautyProductsAdapter
    private lateinit var mainPersonalProductsAdapter: MainPersonalProductsAdapter
    private lateinit var mainWinterWearProductsAdapter: MainWinterWearProductsAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productList: ArrayList<ProductResponse>
    private lateinit var categoryList: ArrayList<CategoryResponse>
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var shimmer_card: ShimmerFrameLayout
    private lateinit var shimmer_rvBeautyProduct: ShimmerFrameLayout
    private lateinit var shimmer_rvPersonal: ShimmerFrameLayout
    private lateinit var shimmer_rvWinter: ShimmerFrameLayout
    private var network by Delegates.notNull<Boolean>()

    //    private lateinit var viewmodel : MainCategoryViewModel
//    private lateinit var mainPersonalProductsAdapter: MainPersonalProductsAdapter
//    private lateinit var mainWinterWearProductsAdapter: MainWinterWearProductsAdapter
//    private var dataFetched by Delegates.notNull<Boolean>()

    private val viewModel: MainCategoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
//        viewModel = ViewModelProvider()[MainCategoryViewModel::class.java]
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSeeAllButton()
        setupCategoryRv()
//        categoryList =ArrayList()
//        categoryList.add(CategoryResponse("",1,"Beauty"," ","beauty","","",0))
//        categoryList.add(CategoryResponse("",2,"Care"," ","beauty","","",0))
//        categoryList.add(CategoryResponse("",3,"Jewels"," ","beauty","","",0))
//        categoryList.add(CategoryResponse("",4,"Cleaning"," ","beauty","","",0))
//        categoryList.add(CategoryResponse("",5,"Home Remedies"," ","beauty","","",0))
//        categoryList.add(CategoryResponse("",6,"Cosmetics"," ","beauty","","",0))
//
//        categoryAdapter.setData(categoryList)
        shimmer_card = requireActivity().findViewById(R.id.shimmer_card)
        shimmer_rvBeautyProduct = requireActivity().findViewById(R.id.shimmer_rvbeauty)
        shimmer_rvPersonal = requireActivity().findViewById(R.id.shimmer_rvPersonal)
        shimmer_rvWinter = requireActivity().findViewById(R.id.shimmer_rvWinterWears)
        sharedPreferences = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE)
        setupMainBeautyProductsRv()
        setupMainPersonalCareProductsRv()
        setupMainWinterWearProductsRv()
        setupSwipeRefreshLayout()
        observeCategories()
        observeBeautyProduct()
        observePersonalProduct()
        observeWinterProduct()
        network = sharedPreferences.getBoolean("network", false)
        if (network) {
            binding.main.visibility = View.VISIBLE

            binding.network.visibility = View.GONE
//            Log.d("data",dataFetched.toString())
//            if (viewModel.beautyState.value == null ||
//                viewModel.catState.value == null ||
//                viewModel.personalState.value == null ||
//                viewModel.winterState.value == null) {
//            }
                viewModel.fetchBeautyProduct()
                viewModel.fetchCategory()
                viewModel.fetchPersonalCareProduct()
                viewModel.fetchWinterWearProduct()
//                Timer().schedule(timerTask {
//                    dataFetched = false
//                }, TimeUnit.MINUTES.toMillis(5))
        } else {
            binding.network.visibility = View.VISIBLE
            binding.main.visibility = View.GONE
            hideLoadingCategories()
            hideLoadingBeauty()
            hideLoadingWinter()
            hideLoadingPersonal()
        }

    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        (activity as? ShoppingActivity)?.forInternetChanged()
        binding.swipeRefresh.isRefreshing = false

//        network = sharedPreferences.getBoolean("network", false)
//        Log.d("network", network.toString())
//        if (network) { // Handle the refresh action here, e.g., fetch new data
//            binding.network.visibility = View.GONE
//            binding.main.visibility = View.VISIBLE
//
        viewModel.fetchCategory()
        viewModel.fetchBeautyProduct()
        viewModel.fetchPersonalCareProduct()
        viewModel.fetchWinterWearProduct()

//
//            // Complete the refresh animation after data is loaded
        binding.swipeRefresh.isRefreshing = false
//        } else {
//            hideLoadingCategories()
//            hideLoadingBeauty()
//            hideLoadingWinter()
//            hideLoadingPersonal()
//            binding.main.visibility = View.GONE
//            binding.network.visibility = View.VISIBLE
//            binding.swipeRefresh.isRefreshing = false
//
//        }

    }

    private fun setupCategoryRv() {
        categoryAdapter = CategoryAdapter(
            requireContext(),
            fragmentManager = requireActivity().supportFragmentManager
        )
        binding.rvCategory.apply {
//         layoutManager = GridLayoutManager(requireContext(),4)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }

    }

    private fun setupMainPersonalCareProductsRv() {
        mainPersonalProductsAdapter = MainPersonalProductsAdapter(
            requireContext(),
            fragmentManager = requireActivity().supportFragmentManager
        )
        binding.rvPersonalProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mainPersonalProductsAdapter
        }
    }

    private fun setupMainWinterWearProductsRv() {
        mainWinterWearProductsAdapter = MainWinterWearProductsAdapter(
            requireContext(),
            fragmentManager = requireActivity().supportFragmentManager
        )
        binding.rvWinterWears.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mainWinterWearProductsAdapter
        }
    }

    private fun setupMainBeautyProductsRv() {
        mainBeautyProductsAdapter = MainBeautyProductsAdapter(
            requireContext(),
            fragmentManager = requireActivity().supportFragmentManager
        )
        binding.rvBeautyProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mainBeautyProductsAdapter
        }
    }

    private fun observeCategories() {

       lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.catState.observe(viewLifecycleOwner) { value ->
                    when (value) {
                        is CategoryEvent.Success -> {
                            hideLoadingCategories()
                            val prodList = value.list
//                            binding.main.visibility =View.VISIBLE

//                            showToast(prodList.toString())
                            categoryAdapter.setData(prodList)
                        }

                        is CategoryEvent.Failure -> {
                            hideLoadingCategories()
//                            binding.nestedScrollMainCategory.visibility =View.GONE

                            showToast("Failed To load data5")
                        }

                        is CategoryEvent.Loading -> {
                            showLoadingCategories()
                        }

                        else -> Unit
                    }
                }
            }
        }

    }

    private fun showLoadingCategories() {
        shimmer_card.startShimmer()
    }

    private fun hideLoadingCategories() {
        shimmer_card.stopShimmer()
        shimmer_card.visibility = View.GONE
    }

    private fun observeBeautyProduct() {

      lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.beautyState.observe(viewLifecycleOwner) { value ->
                    when (value) {
                        is ProductEvent.Success -> {
                            hideLoadingBeauty()
                            val prodList = value.list
//                            showToast(prodList.toString())
//                            binding.main.visibility =View.VISIBLE

                            mainBeautyProductsAdapter.setData(prodList)
                        }

                        is ProductEvent.Failure -> {
//                            binding.main.visibility =View.GONE
//                            binding.nestedScrollMainCategory.visibility =View.GONE

                            hideLoadingBeauty()
                            showToast("Failed To load data1")
                        }

                        is ProductEvent.Loading -> {
                            showLoadingBeauty()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun showLoadingBeauty() {
        shimmer_rvBeautyProduct.startShimmer()
//        shimmer_rvBeautyProduct.visibility =View.VISIBLE
//        binding.shimmerMainContainer.visibility = View.VISIBLE
    }

    private fun hideLoadingBeauty() {
        shimmer_rvBeautyProduct.stopShimmer()
        shimmer_rvBeautyProduct.visibility = View.GONE
//        binding.shimmerMainContainer.visibility = View.GONE
    }

    private fun observePersonalProduct() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.personalState.observe(viewLifecycleOwner) { value ->
                    when (value) {
                        is ProductEvent.Success -> {
                            hideLoadingPersonal()
                            val prodList = value.list
//                            showToast(prodList.toString())
//                            binding.main.visibility =View.VISIBLE
//                            binding.nestedScrollMainCategory.visibility =View.GONE

                            mainPersonalProductsAdapter.setData(prodList)
                        }

                        is ProductEvent.Failure -> {
//                            binding.main.visibility =View.GONE

                            hideLoadingPersonal()
                            showToast("Failed To load data1")
                        }

                        is ProductEvent.Loading -> {
                            showLoadingPersonal()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun showLoadingPersonal() {
        shimmer_rvPersonal.startShimmer()
//        shimmer_rvPersonal.visibility =View.VISIBLE
//        binding.shimmerMainContainer.visibility = View.VISIBLE
    }

    private fun hideLoadingPersonal() {
        shimmer_rvPersonal.stopShimmer()
        shimmer_rvPersonal.visibility = View.GONE
//        binding.shimmerMainContainer.visibility = View.GONE
    }

    private fun observeWinterProduct() {

       lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.winterState.observe(viewLifecycleOwner) { value ->
                    when (value) {
                        is ProductEvent.Success -> {
                            hideLoadingWinter()
                            val prodList = value.list
//                            showToast(prodList.toString())
//                            binding.main.visibility =View.VISIBLE

                            mainWinterWearProductsAdapter.setData(prodList)
                        }

                        is ProductEvent.Failure -> {
//                            binding.main.visibility =View.GONE
//                            binding.nestedScrollMainCategory.visibility =View.GONE

                            hideLoadingWinter()
                            showToast("Failed To load data1")
                        }

                        is ProductEvent.Loading -> {
                            showLoadingWinter()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun showLoadingWinter() {
        shimmer_rvWinter.startShimmer()
        shimmer_rvWinter.visibility = View.VISIBLE
//        binding.shimmerMainContainer.visibility = View.VISIBLE
    }

    private fun hideLoadingWinter() {
        shimmer_rvWinter.stopShimmer()
        shimmer_rvWinter.visibility = View.GONE
//        binding.shimmerMainContainer.visibility = View.GONE

    }

    private fun hideLoading() {


//


    }

    private fun showLoading() {


    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun setupSeeAllButton() {
        binding.buttonSeeAllBeauty.setOnClickListener {
            moveToAnotherFragment(BaseCategoryFragment(), "Beauty")
        }
        binding.buttonSeeAllPersonal.setOnClickListener {
            moveToAnotherFragment(BaseCategoryFragment(), "Personal Care")
        }
        binding.buttonSeeAllWinter.setOnClickListener {
            moveToAnotherFragment(BaseCategoryFragment(), "Winter Wear")
        }
    }

    private fun moveToAnotherFragment(fragment: Fragment, categoryName: String) {
        // Get the FragmentManager
        val fragmentManager = requireActivity().supportFragmentManager
        val bundle = Bundle()
        bundle.putString("categoryName", categoryName)
        fragment.arguments = bundle
        // Begin the FragmentTransaction
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Replace the current fragment with the desired fragment
        fragmentTransaction.replace(R.id.flShopping, fragment)

        // Optional: Add the transaction to the back stack
        fragmentTransaction.addToBackStack(null)

        // Commit the transaction
        fragmentTransaction.commit()
    }

    override fun onStart() {
        super.onStart()
        (activity as? ShoppingActivity)?.forInternetChanged()
    }

    override fun onResume() {
        super.onResume()
        (activity as? ShoppingActivity)?.forInternetChanged()
    }

//    private fun isInternetAvailable(): Boolean {
//        val connectivityManager =
//            getSystemService(requireContext(),ConnectivityManager::class.java) as ConnectivityManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val network = connectivityManager.activeNetwork
//            val capabilities =
//                connectivityManager.getNetworkCapabilities(network)
//            return capabilities != null &&
//                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
//                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
//        } else {
//            val networkInfo = connectivityManager.activeNetworkInfo
//            return networkInfo != null && networkInfo.isConnected
//        }
//    }
}


//
//
//viewLifecycleOwner.lifecycleScope.launch {
//    repeatOnLifecycle(Lifecycle.State.STARTED) {
//        viewModel.showLoading.observe(viewLifecycleOwner) { showLoading ->
//            if (showLoading) {
//                binding.mainCategoryProgressbar.visibility = View.VISIBLE
////                        binding.llbeautyproduct.visibility = View.GONE
////                        binding.rvBeautyProduct.visibility = View.GONE
//            } else {
//                binding.mainCategoryProgressbar.visibility = View.GONE
////                        binding.llbeautyproduct.visibility = View.VISIBLE
////                        binding.rvBeautyProduct.visibility = View.VISIBLE
//
//            }
//        }
//
//    }
//}