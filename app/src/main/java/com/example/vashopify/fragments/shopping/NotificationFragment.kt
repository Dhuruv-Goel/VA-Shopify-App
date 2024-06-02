package com.example.vashopify.fragments.shopping

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vashopify.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class NotificationFragment: Fragment(R.layout.fragment_notification) {




    override fun onResume() {
        super.onResume()

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav?.visibility = View.VISIBLE
    }
}

