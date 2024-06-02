package com.example.vashopify.fragments.shopping

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import com.bumptech.glide.Glide
import com.example.vashopify.BuildConfig
import com.example.vashopify.R
import com.example.vashopify.activities.LoginRegisterActivity
import com.example.vashopify.data.UserResponse
import com.example.vashopify.databinding.FragmentProfileBinding
import com.example.vashopify.fragments.loginRegister.LoginFragment
import com.example.vashopify.fragments.shopping.viewmodel.ProfileViewModel
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.util.Constants.USER_ID
import com.example.vashopify.util.Constants.USER_TOKEN
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: ProfileViewModel by viewModels()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackButtonPressed()
        }
        prefs = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE)
        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment2_to_userAccountFragment)
        }
        binding.linearAllOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment2_to_allOrdersFragment)
        }
        binding.linearLogOut.setOnClickListener {
            showLogOutConfirmationDialog()
        }

        binding.tvVersion.text = "${BuildConfig.VERSION_CODE}"
//        onHomeClick()
//        observeLogOut()
        observeUser()

       try {
           val userName:String = prefs.getString("userName","").toString()
           val userImage:String = prefs.getString("profileImageUrl","").toString()
           if (userName == "") {
               viewModel.getAccount()
           } else {
               showUserInformation(userName, userImage)
               binding.constraintParent.visibility = View.VISIBLE
           }
       } catch (error: Exception){
           //
       }

    }

    private fun onBackButtonPressed() {
         requireActivity().finish()
    }

    private fun showLogOutConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Log Out")
            setMessage("Do you want to log out?")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { dialog, _ ->
//                viewModel.logOut()
                prefs.edit()
                    .putString(USER_TOKEN, null)
                    .putString(USER_ID, null)
                    .putString("userName", "")
                    .putString("profileImageUrl", "")
                    .apply()
                val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
                dialog.dismiss()

            }

        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun observeUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { response ->
                    when (response) {
                        is ProductEvent.Loading -> {
                            binding.progressbarSettings.visibility = View.VISIBLE
                            binding.constraintParent.visibility = View.GONE
                        }

                        is ProductEvent.AuthSuccess -> {
                            binding.progressbarSettings.visibility = View.GONE
                            binding.constraintParent.visibility = View.VISIBLE
//                            Log.d("User", response.user.toString())
                            prefs.edit()
                                .putString("userName", "${response.user.firstName} ${response.user.lastName}")
                                .putString("profileImageUrl", response.user.profileImageUrl)
                                .apply()
                            val userName1 = "${response.user.firstName} ${response.user.lastName}"
                            val userImage1 = response.user.profileImageUrl
                            showUserInformation(userName1,userImage1)
                        }

                        is ProductEvent.Failure -> {
                            binding.progressbarSettings.visibility = View.GONE
                            binding.constraintParent.visibility = View.VISIBLE

                        }

                        is ProductEvent.Unauthorised -> {
                            binding.progressbarSettings.visibility = View.GONE
                            binding.constraintParent.visibility = View.VISIBLE

//                            showToast("Login Again")

                        }

                        else -> Unit
                    }


                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showUserInformation(userName:String,userImage:String) {
        binding.tvUserName.text = userName

        Glide.with(requireContext())
            .load(userImage)
            .error(ColorDrawable(Color.BLACK))
            .into(binding.imageUser)
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

//    private fun observeLogOut() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.logOut.collect { response ->
//                when (response) {
//                    is ProductEvent.Loading -> {
//                        binding.progressbarSettings.visibility = View.VISIBLE
//
//                    }
//
//                    is ProductEvent.UnitSuccess -> {
//                        binding.progressbarSettings.visibility = View.GONE
//
//
//
//                    }
//
//                    is ProductEvent.Failure -> {
//                        binding.progressbarSettings.visibility = View.GONE
//                        showToast("Failure")
//                    }
//
//                    is ProductEvent.Unauthorised -> {
//                        binding.progressbarSettings.visibility = View.GONE
//                        showToast("UnAuthorised")
//                    }
//
//                    else -> Unit
//                }
//            }
//        }
//    }
//
//    private fun onHomeClick() {
//        val btm = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)
//        btm?.menu?.getItem(0)?.setOnMenuItemClickListener {
//            true
//        }
//    }

    override fun onResume() {
        super.onResume()
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation?.visibility = View.VISIBLE

    }
}