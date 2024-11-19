package com.reza.mbahlaptop.ui.main.predict

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PredictViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Predict Fragment"
    }
    val text: LiveData<String> = _text
}