package com.reza.mbahlaptop.ui.main.predict

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reza.mbahlaptop.data.local.ResultRepository
import com.reza.mbahlaptop.data.local.entity.ResultEntity
import com.reza.mbahlaptop.data.remote.ModelRepository
import kotlinx.coroutines.launch

class PredictViewModel(
    private val modelRepository: ModelRepository,
    private val resultRepository: ResultRepository
) : ViewModel() {
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

    fun insertResult(
        date: String,
        os: String,
        processor: String,
        gpu: String,
        ram: Float,
        storageType: String,
        storage: Float,
        displayRes: String,
        lowestPrice: String,
        highestPrice: String,
        brand: String
    ) {
        val result = ResultEntity(
            date = date,
            os = os,
            processor = processor,
            gpu = gpu,
            ram = ram,
            storageType = storageType,
            storage = storage,
            displayRes = displayRes,
            lowestPrice = lowestPrice,
            highestPrice = highestPrice,
            brand = brand
        )

        viewModelScope.launch {
            resultRepository.insertResult(result)
        }
    }

}