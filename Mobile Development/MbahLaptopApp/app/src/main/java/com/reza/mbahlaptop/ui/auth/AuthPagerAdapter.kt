package com.reza.mbahlaptop.ui.auth

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.reza.mbahlaptop.ui.auth.login.LoginFragment
import com.reza.mbahlaptop.ui.auth.register.RegisterFragment

class AuthPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> RegisterFragment()
            else -> throw IllegalStateException("Invalid Fragment Position")
        }
    }
}