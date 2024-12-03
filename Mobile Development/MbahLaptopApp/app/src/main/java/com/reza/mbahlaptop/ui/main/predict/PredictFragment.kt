package com.reza.mbahlaptop.ui.main.predict

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.data.Result
import com.reza.mbahlaptop.databinding.FragmentPredictBinding
import com.reza.mbahlaptop.utils.ViewModelFactory

class PredictFragment : Fragment() {

    private var _binding: FragmentPredictBinding? = null

    private val predictViewModel: PredictViewModel by viewModels<PredictViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPredictBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onResume() {
        super.onResume()

        val osType = resources.getStringArray(R.array.os_type)
        val osArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, osType)
        binding.actvOs.setAdapter(osArrayAdapter)

        val processorWindows = resources.getStringArray(R.array.processor_windows)
        val processorMac = resources.getStringArray(R.array.processor_mac)
        val processorWindowsArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, processorWindows)
        val processorMacArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, processorMac)
        binding.actvProcessor.setAdapter(processorWindowsArrayAdapter)

        binding.actvOs.setOnItemClickListener { parent, _, position, _ ->
            val selectedOS = parent.getItemAtPosition(position) as String
            if (selectedOS == "MAC") {
                binding.actvProcessor.setAdapter(processorMacArrayAdapter)
                binding.actvProcessor.text = null
            } else {
                binding.actvProcessor.setAdapter(processorWindowsArrayAdapter)
                binding.actvProcessor.text = null
            }
        }

        val ramSize = resources.getStringArray(R.array.ram_size)
        val ramSizeArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, ramSize)
        binding.actvRamSize.setAdapter(ramSizeArrayAdapter)

        val storageType = resources.getStringArray(R.array.storage_type)
        val storageTypeArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, storageType)
        binding.actvStorageType.setAdapter(storageTypeArrayAdapter)

        val storageSize = resources.getStringArray(R.array.storage_size)
        val storageSizeArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, storageSize)
        binding.actvStorageSize.setAdapter(storageSizeArrayAdapter)

        val screenRes = resources.getStringArray(R.array.screen_res)
        val screenResArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, screenRes)
        binding.actvScreenRes.setAdapter(screenResArrayAdapter)
    }

    private fun predict(
        brand: String,
        processor: String,
        ram: Float,
        ramType: Float,
        storage: Float,
        storageType: String,
        gpu: String,
        displaySize: Float,
        resolutionWidth: Float,
        resolutionHeight: Float,
        os: String
    ) {
        predictViewModel.uploadSpecs(
            brand,
            processor,
            ram,
            ramType,
            storage,
            storageType,
            gpu,
            displaySize,
            resolutionWidth,
            resolutionHeight,
            os
        ).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Error: ${result.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, "Error: ${result.error}")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "PredictFragment"
    }
}