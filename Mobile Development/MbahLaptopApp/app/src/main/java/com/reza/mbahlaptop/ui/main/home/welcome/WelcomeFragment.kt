package com.reza.mbahlaptop.ui.main.home.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.data.Result
import com.reza.mbahlaptop.databinding.FragmentWelcomeBinding
import com.reza.mbahlaptop.ui.main.history.HistoryViewModel
import com.reza.mbahlaptop.utils.ViewModelFactory

class WelcomeFragment : Fragment() {
    private lateinit var _binding: FragmentWelcomeBinding
    private val binding get() = _binding

    private lateinit var factory: ViewModelFactory
    private val historyViewModel: HistoryViewModel by viewModels {
        factory
    }

    private val recentHistoryAdapter = RecentHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = ViewModelFactory.getInstance(requireContext())

        setupView()

        binding.btnPredictHistory.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_history)
        }
    }

    private fun setupView() {
        historyViewModel.getRecentResults().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val resultData = result.data
                        recentHistoryAdapter.submitList(resultData)
                        binding.rvHistoryList.apply {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = recentHistoryAdapter
                        }
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(
                            requireView(),
                            "Error Occurred: ${result.error}",
                            Snackbar.LENGTH_SHORT
                        ).setAction("Dismiss") {}.show()
                    }
                }
            }
        }
    }
}