package com.example.androidlearnings.threading

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.example.androidlearnings.repository.WordRepository
import com.example.androidlearnings.room.LearningDatabase
import com.example.androidlearnings.room.Word
import com.example.androidlearnings.utils.WordConstant
import java.lang.NullPointerException

const val TAG = "ProcessWordThread"

class ProcessWordThread(var mainThreadHandler: Handler?, var application: Application) : Thread() {

    private lateinit var handler: CustomHandler
    private var isRunning = false
    private lateinit var wordRepository: WordRepository

    init {
        isRunning = true
    }

    override fun run() {
        if (isRunning) {
            Looper.prepare()
            handler = CustomHandler(Looper.myLooper())
            wordRepository = WordRepository(application)
            Looper.loop()
        }
    }

    fun quitThread() {
        isRunning = false
        mainThreadHandler = null
    }

    fun insertWord(word: Word) {
        wordRepository.insertWord(word)
    }

    fun updateWord(word: Word) {
        wordRepository.updateWord(word)
    }

    fun deleteWord(word: Word) {
        wordRepository.deleteWord(word)
    }

    fun sendMessageToBackgroundThread(message: Message) {
        while (true) {
            try {
                sleep(400)
//                if(::handler.isInitialized) {
                handler.sendMessage(message)
//                }
                break
            } catch (e: NullPointerException) {
                Log.d(TAG, "sendMessageToBackgroundThread: Null Pointer ${e.printStackTrace()}")
                try {
                    sleep(200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private inner class CustomHandler(looper: Looper?) : Handler(looper) {

        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                WordConstant.INSERT_WORD -> {
                    Log.d(TAG, "handleMessage: saving word on thread: ${currentThread().name}")

                    var bundle = msg.data
                    val word = bundle.getParcelable<Word>(WordConstant.INSERT_WORD_OBJECT)
                    var status = wordRepository.insertWord(word)

                    var message: Message
                    if (status != -1L) {
                        message = Message.obtain(null, WordConstant.INSERT_WORD_SUCCESS)
                    } else {
                        message = Message.obtain(null, WordConstant.INSERT_WORD_FAIL)
                    }

                    mainThreadHandler?.sendMessage(message)

                }
                WordConstant.UPDATE_WORD -> {
                    Log.d(TAG, "handleMessage: update word on thread: ${currentThread().name}")

                    var bundle = msg.data
                    val word = bundle.getParcelable<Word>(WordConstant.UPDATE_WORD_OBJECT)
                    var status = wordRepository.updateWord(word)

                    var message: Message
                    if (status > 0) {
                        message = Message.obtain(null, WordConstant.UPDATE_WORD_SUCCESS)
                    } else {
                        message = Message.obtain(null, WordConstant.UPDATE_WORD_FAIL)
                    }

                    mainThreadHandler?.sendMessage(message)

                }
                WordConstant.DELETE_WORD -> {
                    Log.d(TAG, "handleMessage: delete word on thread: ${currentThread().name}")

                    var bundle = msg.data
                    val word: Word = bundle.getParcelable(WordConstant.DELETE_WORD_OBJECT)
                    var status = wordRepository.deleteWord(word)

                    var message: Message
                    if(status > 0) {
                        message = Message.obtain(null, WordConstant.DELETE_WORD_SUCCESS)
                    }else {
                        message = Message.obtain(null, WordConstant.DELETE_WORD_FAIL)
                    }

                    mainThreadHandler?.sendMessage(message)

                }
                WordConstant.RETRIEVE_WORD -> {
                    Log.d(TAG, "handleMessage: retrieve word on thread: ${currentThread().name}")

                    Bundle().apply {

                        var wordList = ArrayList(wordRepository.retriveAllWords())
                        var message: Message

                        wordList?.let {
                            if (it.size > 0) {
                                putParcelableArrayList(WordConstant.WORD_LIST, wordList)
                                message = Message.obtain(null, WordConstant.RETRIEVE_WORD_SUCCESS)
                                message.data = this
                                mainThreadHandler?.sendMessage(message)
                            } else {
                                message = Message.obtain(null, WordConstant.RETRIEVE_WORD_FAIL)
                                mainThreadHandler?.sendMessage(message)
                            }
                        }
                    }
                }
            }
        }
    }
}