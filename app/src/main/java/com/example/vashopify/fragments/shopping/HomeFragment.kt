package com.example.vashopify.fragments.shopping

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.vashopify.R
import com.example.vashopify.activities.AddressActivity
import com.example.vashopify.activities.SearchActivity
import com.example.vashopify.databinding.FragmentHomeBinding
import com.example.vashopify.fragments.categories.MainCategoryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.IOException

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        replaceFragment(R.id.flHome, MainCategoryFragment())


        binding.btnLocation.setOnClickListener {
           try {
                val intent = Intent(requireContext(), AddressActivity::class.java)

                Log.d("start", "ButtonClicked  $intent")
                startActivity(intent)
            }catch(e:IOException){
                Log.e("error",e.message.toString())
            }
        }
        binding.btnSearchHome.setOnClickListener {
//            replaceFragment(R.id.flSearch,SearchFragment())
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
//        val categoriesFragments = arrayListOf<Fragment>(
//            MainCategoryFragment(),
//            BeautyFragment(),
//            PersonalCareFragment(),
//            BathSoapFragment(),
//            PerfumesFragment(),
//            WinterWearFragment()
//        )
//
//        binding.viewpagerHome.isUserInputEnabled = false
//
//        val viewPager2Adapter =
//            HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
//        binding.viewpagerHome.adapter = viewPager2Adapter
//        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome) { tab, position ->
//            when (position) {
//                0 -> tab.text = "Main"
//                1 -> tab.text = "Beauty"
//                2 -> tab.text = "Personal Care"
//                3 -> tab.text = "Home Cleaning"
//                4 -> tab.text = "Perfumes & Deo"
//                5 -> tab.text = "Winter Wears"
//
//            }
//        }.attach()
    }

    private fun replaceFragment(id1: Int, fragment: Fragment) {
        // Get the FragmentManager
        val fragmentManager = requireActivity().supportFragmentManager

        // Begin the FragmentTransaction
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Replace the current fragment with the desired fragment
        fragmentTransaction.replace(id1, fragment)

        // Optional: Add the transaction to the back stack
//        fragmentTransaction.addToBackStack(null)

        // Commit the transaction
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav?.visibility = View.VISIBLE
    }
}