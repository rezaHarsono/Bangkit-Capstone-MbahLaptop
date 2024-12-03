package com.reza.mbahlaptop.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.reza.mbahlaptop.data.Result
import com.reza.mbahlaptop.data.remote.response.ModelResponse
import com.reza.mbahlaptop.data.remote.retrofit.model.ApiServiceModel
import com.reza.mbahlaptop.data.remote.retrofit.model.SpecRequest

class ModelRepository private constructor(
    private val apiService: ApiServiceModel,
) {
    fun uploadSpecs(
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
    ): LiveData<Result<ModelResponse>> = liveData {
        emit(Result.Loading)
        try {
            val request = SpecRequest(
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
            )
            val response = apiService.uploadSpecs(request)
            if (response.status?.code == 200) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.status?.message.toString()))
                Log.e(TAG, "Error: ${response.status?.message.toString()}")
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
            Log.e(TAG, "Error: ${e.message.toString()}")
        }
    }

    companion object {
        const val TAG = "Model Repository"

        @Volatile
        private var INSTANCE: ModelRepository? = null
        fun getInstance(apiService: ApiServiceModel): ModelRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ModelRepository(apiService)
            }.also { INSTANCE = it }
    }
}