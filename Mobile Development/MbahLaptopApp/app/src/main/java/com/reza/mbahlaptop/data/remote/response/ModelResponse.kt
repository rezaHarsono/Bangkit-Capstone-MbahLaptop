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

data class PredictedIntervals(

	@field:SerializedName("Quantile 0.75")
	val quantile075: Any? = null,

	@field:SerializedName("Quantile 0.5")
	val quantile05: Any? = null,

	@field:SerializedName("Quantile 0.25")
	val quantile025: Any? = null
)

data class Data(

	@field:SerializedName("predicted_intervals")
	val predictedIntervals: PredictedIntervals? = null
)
