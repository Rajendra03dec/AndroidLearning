package com.example.androidlearnings.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "word")
data class Word(@PrimaryKey(autoGenerate = true)
                val id: Int = 0,
                @ColumnInfo(name = "title")
                val title: String,
                @ColumnInfo(name = "content")
                val content: String,
                @ColumnInfo(name = "timestamp")
                val timestamp: String) : Parcelable