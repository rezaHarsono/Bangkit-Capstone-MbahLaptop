package com.reza.mbahlaptop.data.remote.response

import com.google.gson.annotations.SerializedName

data class ModelResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("status")
    val status: Status? = null
)

data class Status(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Data(

    @field:SerializedName("predicted_price")
    val predictedPrice: Any? = null
)