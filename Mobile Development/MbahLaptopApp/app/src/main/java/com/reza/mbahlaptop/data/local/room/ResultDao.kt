package com.reza.mbahlaptop.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reza.mbahlaptop.data.local.entity.ResultEntity

@Dao
interface ResultDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertResult(result: ResultEntity)

    @Query("DELETE FROM result")
    suspend fun deleteAllResult()

    @Query("SELECT * FROM result ORDER BY id DESC ")
    fun getAllResult(): LiveData<List<ResultEntity>>

    @Query("SELECT * FROM result ORDER BY id DESC LIMIT 1")
    fun getRecentResults(): LiveData<List<ResultEntity>>
}