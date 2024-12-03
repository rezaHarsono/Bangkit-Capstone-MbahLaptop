package com.reza.mbahlaptop.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reza.mbahlaptop.data.remote.ModelRepository
import com.reza.mbahlaptop.di.ModelInjection
import com.reza.mbahlaptop.ui.main.predict.PredictViewModel

class ViewModelFactory private constructor(private val modelRepository: ModelRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PredictViewModel::class.java)) {
            return PredictViewModel(modelRepository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class: " + modelClass.name)
    }

    companion object {
        fun getInstance(context: Context) =
            ViewModelFactory(ModelInjection.provideRepository(context))
    }
}