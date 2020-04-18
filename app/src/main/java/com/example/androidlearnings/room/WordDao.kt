package com.example.androidlearnings.room

import androidx.room.*

@Dao
interface WordDao {

    @Insert
    fun insertWord(word: Word): Long

    @Update
    fun updateWord(word: Word): Int

    @Delete
    fun deleteWord(word: Word): Int

    @Query("Select * from word")
    fun getAllWords(): List<Word>
}