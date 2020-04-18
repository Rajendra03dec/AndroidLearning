package com.example.androidlearnings.repository

import android.app.Application
import com.example.androidlearnings.room.LearningDatabase
import com.example.androidlearnings.room.Word
import com.example.androidlearnings.room.WordDao

class WordRepository {

    var wordDao: WordDao
    var allWords: List<Word>

    constructor(application: Application) {
        var learningDatabase = LearningDatabase.getInstance(application)
        wordDao = learningDatabase.wordDao()
        allWords = wordDao.getAllWords()
    }

    fun insertWord(word: Word): Long {
        return wordDao.insertWord(word)
    }

    fun updateWord(word: Word): Int {
        return wordDao.updateWord(word)
    }

    fun deleteWord(word: Word): Int {
        return wordDao.deleteWord(word)
    }

    fun retriveAllWords(): List<Word> {
        return allWords
    }
}