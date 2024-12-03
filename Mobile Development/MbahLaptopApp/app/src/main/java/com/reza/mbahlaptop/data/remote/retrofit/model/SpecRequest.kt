package com.reza.mbahlaptop.data.remote.retrofit.model

data class SpecRequest(
    val brand: String,
    val processor: String,
    val ram: Float,
    val ramType: Float,
    val storage: Float,
    val storageType: String,
    val gpu: String,
    val displaySize: Float,
    val resolutionWidth: Float,
    val resolutionHeight: Float,
    val os: String
)