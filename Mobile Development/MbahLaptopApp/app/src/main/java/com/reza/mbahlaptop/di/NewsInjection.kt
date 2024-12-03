package com.reza.mbahlaptop.di

import com.reza.mbahlaptop.data.remote.NewsRepository
import com.reza.mbahlaptop.data.remote.retrofit.news.ApiConfig

object NewsInjection {
    fun provideRepository(): NewsRepository {
        val apiService = ApiConfig.getApiService()
        return NewsRepository.getInstance(apiService)
    }
}