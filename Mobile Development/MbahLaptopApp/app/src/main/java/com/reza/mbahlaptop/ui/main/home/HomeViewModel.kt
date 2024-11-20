package com.reza.mbahlaptop.ui.main.home

import androidx.lifecycle.ViewModel
import com.reza.mbahlaptop.data.remote.NewsRepository

class HomeViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun getNews() = newsRepository.getNews()
}