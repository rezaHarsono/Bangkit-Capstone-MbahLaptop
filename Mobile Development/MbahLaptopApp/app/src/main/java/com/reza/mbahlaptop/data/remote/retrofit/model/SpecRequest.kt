package com.reza.mbahlaptop.data.remote.retrofit.model

data class SpecRequest(
    val brand: String,
    val processor: String,
    val Ram: Float,
    val Storage: Float,
    val storage_type: String,
    val gpu: String,
    val display_size: Float,
    val resolution_width: Float,
    val resolution_height: Float,
    val os: String
)