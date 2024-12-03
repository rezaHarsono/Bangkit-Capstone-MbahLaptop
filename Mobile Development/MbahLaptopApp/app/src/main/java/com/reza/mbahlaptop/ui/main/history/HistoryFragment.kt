package com.reza.mbahlaptop.ui.main.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.reza.mbahlaptop.databinding.FragmentHistoryBinding
import com.reza.mbahlaptop.ui.result.ResultActivity

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!

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
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            includedLayoutA.btnPredictionDetail.setOnClickListener {
                goToDetail()
            }
        }
    }

    private fun goToDetail() {
        val intent = Intent(requireContext(), ResultActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}