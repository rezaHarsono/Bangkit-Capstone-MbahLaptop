package com.reza.mbahlaptop.ui.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.reza.mbahlaptop.ui.main.home.news.NewsFragment
import com.reza.mbahlaptop.ui.main.home.welcome.WelcomeFragment

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WelcomeFragment()
            1 -> NewsFragment()
            else -> throw IllegalStateException("Invalid Fragment Position")
        }
    }
}