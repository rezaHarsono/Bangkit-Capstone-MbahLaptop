package com.reza.mbahlaptop.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.reza.mbahlaptop.data.Result
import com.reza.mbahlaptop.data.local.entity.ResultEntity
import com.reza.mbahlaptop.data.local.room.ResultDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResultRepository private constructor(
    private val resultDao: ResultDao
) {
    fun getAllEvents(): LiveData<Result<List<ResultEntity>>> = liveData {
        emit(Result.Loading)
        val localData: LiveData<Result<List<ResultEntity>>> =
            resultDao.getAllResult().map { Result.Success(it) }
        emitSource(localData)
    }

    suspend fun deleteAllResult() {
        withContext(Dispatchers.IO) {
            resultDao.deleteAllResult()
        }
    }

    suspend fun insertResult(result: ResultEntity) {
        withContext(Dispatchers.IO) {
            resultDao.insertResult(result)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ResultRepository? = null
        fun getInstance(
            resultDao: ResultDao
        ): ResultRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ResultRepository(resultDao)
            }.also { INSTANCE = it }
    }
}