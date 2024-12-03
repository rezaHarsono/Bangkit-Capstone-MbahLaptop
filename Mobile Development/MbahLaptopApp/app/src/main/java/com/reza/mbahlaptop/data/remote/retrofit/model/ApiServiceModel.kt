package com.reza.mbahlaptop.data.remote.retrofit.model

import com.reza.mbahlaptop.data.remote.response.ModelResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceModel {
    @POST("predict")
    suspend fun uploadSpecs(
        @Body specs: SpecRequest
    ): ModelResponse
}