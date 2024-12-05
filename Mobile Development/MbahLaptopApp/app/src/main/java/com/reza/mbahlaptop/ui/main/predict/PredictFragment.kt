package com.reza.mbahlaptop.ui.main.predict

import android.content.Intent
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
import com.reza.mbahlaptop.ui.result.ResultActivity
import com.reza.mbahlaptop.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PredictFragment : Fragment() {

    private var _binding: FragmentPredictBinding? = null
    private val binding get() = _binding!!

    private val predictViewModel: PredictViewModel by viewModels<PredictViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val resolutionMap = mapOf(
        "HD" to Pair(1366f, 768f),
        "FHD" to Pair(1920f, 1080f),
        "2K" to Pair(2560f, 1440f)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPredictBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
    }

    private fun setupAction() {
        binding.buttonPredict.setOnClickListener {
            val os = binding.actvOs.text.toString()
            val processor = binding.actvProcessor.text.toString()
            val ramString = binding.actvRamSize.text.toString()
            val gpu = binding.actvGpu.text.toString()
            val storageType = binding.actvStorageType.text.toString()
            val storageSizeString = binding.actvStorageSize.text.toString()
            val resolution = binding.actvScreenRes.text.toString()
            val resolutionPair = resolutionMap[resolution] ?: Pair(0f, 0f)

            if (emptyFieldIsEmpty()) {
               showToast(getString(R.string.fields_empty_warning))
            } else {
                try {
                    val ram = ramString.toFloat()
                    val storageSize = storageSizeString.toFloat()
                    val resolutionWidth = resolutionPair.first
                    val resolutionHeight = resolutionPair.second

                    predict(
                        processor = processor,
                        ram = ram,
                        storage = storageSize,
                        storageType = storageType,
                        gpu = gpu,
                        resolutionWidth = resolutionWidth,
                        resolutionHeight = resolutionHeight,
                        os = os
                    )
                } catch (e: NumberFormatException) {
                    showToast(getString(R.string.fields_empty_warning))
                    Log.e(TAG, "Error: ${e.message}")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val osType = resources.getStringArray(R.array.os_type)
        val osArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, osType)
        binding.actvOs.setAdapter(osArrayAdapter)

        val processorWindows = resources.getStringArray(R.array.processor_windows)
        val processorMac = resources.getStringArray(R.array.processor_mac)
        val gpuWindows = resources.getStringArray(R.array.gpu_windows)
        val gpuMac = resources.getStringArray(R.array.gpu_mac)
        val processorWindowsArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, processorWindows)
        val processorMacArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, processorMac)
        binding.actvProcessor.setAdapter(processorWindowsArrayAdapter)
        val gpuWindowsArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, gpuWindows)
        val gpuMacArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, gpuMac)
        binding.actvGpu.setAdapter(gpuWindowsArrayAdapter)

        binding.actvOs.setOnItemClickListener { parent, _, position, _ ->
            val selectedOS = parent.getItemAtPosition(position) as String
            if (selectedOS == "MAC") {
                binding.actvProcessor.setAdapter(processorMacArrayAdapter)
                binding.actvGpu.setAdapter(gpuMacArrayAdapter)
                binding.actvProcessor.text = null
                binding.actvGpu.text = null
            } else {
                binding.actvProcessor.setAdapter(processorWindowsArrayAdapter)
                binding.actvGpu.setAdapter(gpuWindowsArrayAdapter)
                binding.actvProcessor.text = null
                binding.actvGpu.text = null
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
        processor: String,
        ram: Float,
        storage: Float,
        storageType: String,
        gpu: String,
        resolutionWidth: Float,
        resolutionHeight: Float,
        os: String
    ) {
        predictViewModel.uploadSpecs(
            brand = "Asus",
            processor = processor,
            ram = ram,
            ramType = 1f,
            storage = storage,
            storageType = storageType,
            gpu = gpu,
            displaySize = 15.6f,
            resolutionWidth = resolutionWidth,
            resolutionHeight = resolutionHeight,
            os = os
        ).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        val date = getCurrentDate()
                        val intent = Intent(requireContext(), ResultActivity::class.java)
                        val bundle = Bundle().apply {
                            putString("date", date)
                            putString("os", binding.actvOs.text.toString())
                            putString("processor", binding.actvProcessor.text.toString())
                            putString("ram", binding.actvRamSize.text.toString())
                            putString("gpu", binding.actvGpu.text.toString())
                            putString("storageType", binding.actvStorageType.text.toString())
                            putString("storageSize", binding.actvStorageSize.text.toString())
                            putString("resolution", binding.actvScreenRes.text.toString())
                            putString(
                                "lowPrice",
                                result.data.data?.predictedIntervals?.quantile025.toString()
                            )
                            putString(
                                "highPrice",
                                result.data.data?.predictedIntervals?.quantile075.toString()
                            )
                        }
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Error -> {
                        showLoading(false)
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

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    private fun emptyFieldIsEmpty(): Boolean {
        return listOf(
            binding.actvOs.text,
            binding.actvProcessor.text,
            binding.actvRamSize.text,
            binding.actvGpu.text,
            binding.actvStorageType.text,
            binding.actvStorageSize.text,
            binding.actvScreenRes.text
        ).all { it.isEmpty() }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "PredictFragment"
    }
}