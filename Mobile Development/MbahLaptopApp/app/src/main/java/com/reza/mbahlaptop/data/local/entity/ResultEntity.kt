package com.reza.mbahlaptop.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "result")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "os")
    val os: String,

    @ColumnInfo(name = "processor")
    val processor: String,

    @ColumnInfo(name = "gpu")
    val gpu: String,

    @ColumnInfo(name = "ram")
    val ram: Float,

    @ColumnInfo(name = "storageType")
    val storageType: String,

    @ColumnInfo(name = "storage")
    val storage: Float,

    @ColumnInfo(name = "displayRes")
    val displayRes: String,

    @ColumnInfo(name = "lowestPrice")
    val lowestPrice: String,

    @ColumnInfo(name = "highestPrice")
    val highestPrice: String,

    @ColumnInfo(name = "brand")
    val brand: String
) : Parcelable
