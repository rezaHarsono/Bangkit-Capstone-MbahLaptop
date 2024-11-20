package com.reza.mbahlaptop.data.remote.retrofit

import com.reza.mbahlaptop.data.remote.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything?q=laptop&language=en&sortBy=publishedAt")
    suspend fun getNews(
        @Query("apiKey") apiKey: String
    ): NewsResponse
}