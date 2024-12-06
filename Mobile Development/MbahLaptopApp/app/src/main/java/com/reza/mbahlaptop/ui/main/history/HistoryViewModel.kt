package com.reza.mbahlaptop.ui.main.history

import androidx.lifecycle.ViewModel
import com.reza.mbahlaptop.data.local.ResultRepository

class HistoryViewModel(private val resultRepository: ResultRepository) : ViewModel() {
    fun getAllResult() = resultRepository.getAllEvents()
}