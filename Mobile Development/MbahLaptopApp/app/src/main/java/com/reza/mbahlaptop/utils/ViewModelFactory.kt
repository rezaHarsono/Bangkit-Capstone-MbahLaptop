package com.reza.mbahlaptop.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reza.mbahlaptop.data.local.ResultRepository
import com.reza.mbahlaptop.data.remote.ModelRepository
import com.reza.mbahlaptop.di.ModelInjection
import com.reza.mbahlaptop.ui.main.history.HistoryViewModel
import com.reza.mbahlaptop.ui.main.predict.PredictViewModel

class ViewModelFactory private constructor(
    private val modelRepository: ModelRepository,
    private val resultRepository: ResultRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PredictViewModel::class.java) -> {
                PredictViewModel(modelRepository, resultRepository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(resultRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown viewmodel class: " + modelClass.name)

        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory {
            val (modelRepository, resultRepositroy) = ModelInjection.provideRepository(context)
            return ViewModelFactory(
                modelRepository, resultRepositroy
            )
        }

    }
}