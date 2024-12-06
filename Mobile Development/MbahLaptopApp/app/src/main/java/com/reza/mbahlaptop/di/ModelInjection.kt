package com.reza.mbahlaptop.di

import android.content.Context
import com.reza.mbahlaptop.data.local.ResultRepository
import com.reza.mbahlaptop.data.local.room.ResultDatabase
import com.reza.mbahlaptop.data.remote.ModelRepository
import com.reza.mbahlaptop.data.remote.retrofit.model.ApiConfigModel

object ModelInjection {
    fun provideRepository(context: Context): Pair<ModelRepository, ResultRepository> {
        val apiService = ApiConfigModel.getApiService()
        val database = ResultDatabase.getInstance(context)
        val resultDao = database.resultDao()

        val modelRepository = ModelRepository.getInstance(apiService)
        val resultRepository = ResultRepository.getInstance(resultDao)
        return Pair(modelRepository, resultRepository)
    }
}