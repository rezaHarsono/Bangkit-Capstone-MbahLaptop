package com.reza.mbahlaptop.di

import android.content.Context
import com.reza.mbahlaptop.data.remote.ModelRepository
import com.reza.mbahlaptop.data.remote.retrofit.model.ApiConfigModel

object ModelInjection {
    fun provideRepository(context: Context): ModelRepository {
        val apiService = ApiConfigModel.getApiService()
        return ModelRepository.getInstance(apiService)
    }
}