package com.reza.mbahlaptop.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.databinding.FragmentHomeBinding
import com.reza.mbahlaptop.utils.updateHeight

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        binding?.apply {
            btnPredict.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_predict)
            }
        }

    }

    private fun setupView() {
        binding?.apply {
            val sectionsPagerAdapter = HomePagerAdapter(requireParentFragment())
            val viewPager: ViewPager2 = viewPager
            val tabs: TabLayout = tabs

            viewPager.adapter = sectionsPagerAdapter
            viewPager.updateHeight()

            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewPager.updateHeight()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.title_home,
            R.string.title_news
        )
    }
}