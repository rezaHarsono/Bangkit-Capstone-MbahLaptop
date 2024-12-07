package com.reza.mbahlaptop.ui.main.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reza.mbahlaptop.data.local.ResultRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val resultRepository: ResultRepository) : ViewModel() {
    fun getAllResult() = resultRepository.getAllEvents()

    fun deleteAllResult() {
        viewModelScope.launch {
            resultRepository.deleteAllResult()
        }
    }
}