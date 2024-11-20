package com.reza.mbahlaptop.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reza.mbahlaptop.data.remote.NewsRepository
import com.reza.mbahlaptop.di.NewsInjection

class HomeViewModelFactory private constructor(private val newsRepository: NewsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class" + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: HomeViewModelFactory? = null
        fun getInstance(): HomeViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeViewModelFactory(NewsInjection.provideRepository())
            }.also { INSTANCE = it }
    }
}