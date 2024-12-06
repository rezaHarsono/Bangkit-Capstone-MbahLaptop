package com.reza.mbahlaptop.ui.main.predict

import androidx.lifecycle.ViewModel
import com.reza.mbahlaptop.data.remote.ModelRepository

class PredictViewModel(private val modelRepository: ModelRepository) : ViewModel() {
    fun uploadSpecs(
        brand: String,
        processor: String,
        ram: Float,
        storage: Float,
        storageType: String,
        gpu: String,
        displaySize: Float,
        resolutionWidth: Float,
        resolutionHeight: Float,
        os: String
    ) = modelRepository.uploadSpecs(
        brand,
        processor,
        ram,
        storage,
        storageType,
        gpu,
        displaySize,
        resolutionWidth,
        resolutionHeight,
        os
    )
}