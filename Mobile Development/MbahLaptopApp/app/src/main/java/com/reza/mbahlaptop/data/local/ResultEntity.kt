package com.reza.mbahlaptop.data.local

data class ResultEntity(
    val id: Int = 0,
    val os: String,
    val cpu: String,
    val gpu: String,
    val ram: String,
    val storageType: String,
    val storageSize: String
)
