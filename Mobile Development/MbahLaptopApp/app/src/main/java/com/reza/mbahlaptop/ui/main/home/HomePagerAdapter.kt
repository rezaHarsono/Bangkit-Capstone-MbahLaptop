package com.reza.mbahlaptop.ui.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.reza.mbahlaptop.ui.main.home.dashboard.DashboardFragment
import com.reza.mbahlaptop.ui.main.home.news.NewsFragment

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DashboardFragment()
            1 -> NewsFragment()
            else -> throw IllegalStateException("Invalid Fragment Position")
        }
    }
}