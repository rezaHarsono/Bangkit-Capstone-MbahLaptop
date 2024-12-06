package com.reza.mbahlaptop.ui.main.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.reza.mbahlaptop.data.Result
import com.reza.mbahlaptop.databinding.FragmentHistoryBinding
import com.reza.mbahlaptop.ui.result.ResultActivity
import com.reza.mbahlaptop.utils.ViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val historyViewModel: HistoryViewModel by viewModels {
        factory
    }

    private val historyAdapter = HistoryAdapter(
        onDetailClick = { result ->
            val intent = Intent(requireContext(), ResultActivity::class.java)
            intent.putExtra("RESULT", result)
            startActivity(intent)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = ViewModelFactory.getInstance(requireContext())

        setupView()
    }

    private fun setupView() {
        historyViewModel.getAllResult().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val resultData = result.data
                        historyAdapter.submitList(resultData)
                        binding.rvResultList.apply {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = historyAdapter
                        }
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
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
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}