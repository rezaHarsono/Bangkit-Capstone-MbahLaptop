package com.reza.mbahlaptop.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.reza.mbahlaptop.BuildConfig.API_KEY
import com.reza.mbahlaptop.data.Result
import com.reza.mbahlaptop.data.remote.response.ArticlesItem
import com.reza.mbahlaptop.data.remote.retrofit.news.ApiService

class NewsRepository private constructor(
    private val apiService: ApiService
) {

    fun getNews(): LiveData<Result<List<ArticlesItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getNews(API_KEY)
            val newsData = response.articles?.filterNotNull()
            val news = newsData?.filter { result ->
                result.title != "[Removed]"
            }?.take(5)
            if (news != null) {
                emit(Result.Success(news))
            } else {
                emit(Result.Error("No News available"))
            }
        } catch (e: Exception) {
            Log.e("News Repository", "getNews: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: NewsRepository? = null
        fun getInstance(apiService: ApiService): NewsRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NewsRepository(apiService)
            }.also { INSTANCE = it }
    }
}