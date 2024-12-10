package com.reza.mbahlaptop.ui.main.home.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.data.Result
import com.reza.mbahlaptop.databinding.FragmentNewsBinding
import com.reza.mbahlaptop.ui.main.home.HomeViewModel
import com.reza.mbahlaptop.ui.main.home.HomeViewModelFactory
import com.reza.mbahlaptop.utils.updateHeight


class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding

    private val factory: HomeViewModelFactory = HomeViewModelFactory.getInstance()
    private val viewModel: HomeViewModel by viewModels {
        factory
    }

    private val newsAdapter = NewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayData()
    }

    private fun displayData() {
        viewModel.getNews().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        Log.d("LOADING", "Loading")
                        binding?.shimmerNews?.visibility = View.VISIBLE
                        binding?.shimmerNews?.startShimmer()
                    }

                    is Result.Success -> {
                        Log.d("SUCCESS", "Success")
                        binding?.shimmerNews?.stopShimmer()
                        binding?.shimmerNews?.visibility = View.GONE
                        val newsData = result.data
                        newsAdapter.submitList(newsData)
                        binding!!.rvNewsList.apply {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = newsAdapter
                            adapter?.registerAdapterDataObserver(object :
                                RecyclerView.AdapterDataObserver() {
                                override fun onChanged() {
                                    super.onChanged()
                                    (parentFragment?.view?.findViewById<ViewPager2>(R.id.view_pager))?.updateHeight()
                                }
                            })
                        }
                    }

                    is Result.Error -> {
                        binding?.shimmerNews?.stopShimmer()
                        binding?.shimmerNews?.visibility = View.GONE
                        view?.let {
                            Snackbar.make(
                                requireActivity(),
                                it,
                                "Error Occurred: ${result.error}",
                                Snackbar.LENGTH_SHORT
                            ).setAction("Dismiss") {
                            }.show()
                        }
                    }
                }
            }
        }
    }
}