package com.example.vashopify.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.cloudinary.android.MediaManager
import com.example.vashopify.BuildConfig
import com.example.vashopify.CloudinaryConfig
import com.example.vashopify.R
import com.example.vashopify.databinding.ActivityShoppingBinding
import com.example.vashopify.fragments.shopping.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }
    private val isInternetAvailableLiveData = MutableLiveData<Boolean>()
    private lateinit var sharedPreferences: SharedPreferences


    private val viewModel: CartViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
        fetchCount()
        observeNetworkChanges()
        checkInternetStatus()
        observeInternet()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCart()
    }


    private fun fetchCount() {
        lifecycleScope.launch {
            viewModel.cartItems.collect { list ->

                val count = list.items.size
                Log.d("count", count.toString())
                val bottomNavigationView =
                    findViewById<BottomNavigationView>(R.id.bottomNavigation)
                if (count != 0) {
                    bottomNavigationView.getOrCreateBadge(R.id.cartFragment2).apply {
                        number = count
                        backgroundColor =
                            ContextCompat.getColor(this@ShoppingActivity, R.color.g_blue)

                    }
                }
                if (count == 0) {
                    bottomNavigationView.removeBadge(R.id.cartFragment2)
                }


            }
        }

    }

    @SuppressLint("ObsoleteSdkInt")
    private fun observeNetworkChanges() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isInternetAvailableLiveData.postValue(true)
            }

            override fun onLost(network: Network) {
                isInternetAvailableLiveData.postValue(false)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkInternetStatus() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities =
                connectivityManager.getNetworkCapabilities(network)
            val isConnected = capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            isInternetAvailableLiveData.postValue(isConnected)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            val isConnected = networkInfo != null && networkInfo.isConnected
            isInternetAvailableLiveData.postValue(isConnected)
        }


    }

    private fun observeInternet(){
        isInternetAvailableLiveData.observe(this) { isConnected ->
            if (isConnected) {
                // Internet is available, proceed with your app logic
                sharedPreferences.edit()
                    .putBoolean("network", true)
                    .apply()
            } else {
                // No internet connection, show a message or take appropriate action
                sharedPreferences.edit()
                    .putBoolean("network", false)
                    .apply()
            }
        }
    }


    fun forCounterChanged() {
        viewModel.getCart()
    }

    fun forInternetChanged() {
        observeNetworkChanges()
        checkInternetStatus()
        observeInternet()
    }



}