package com.example.androidlearnings.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import com.example.androidlearnings.R
import com.example.androidlearnings.room.Word
import com.example.androidlearnings.threading.ProcessWordThread
import com.example.androidlearnings.utils.WordConstant
import kotlinx.android.synthetic.main.activity_add_word.*
import kotlinx.android.synthetic.main.activity_new_note.*

class AddWordActivity : AppCompatActivity(), Handler.Callback {

    lateinit var processWordThread: ProcessWordThread
    lateinit var mainThreadhandler: Handler
    lateinit var title: String
    lateinit var content: String
    lateinit var wordModel: Word

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)

        mainThreadhandler = Handler(this)

        var bundle = intent?.extras

        bundle?.let {
            wordModel = it.getParcelable(WordConstant.EDIT_WORD_OBJECT)

            wordModel?.apply {

                saveWord.text = getString(R.string.update)
                wordTitle.setText(title)
                wordContent.setText(content)
                timestampValue.text = timestamp
            }
        }

        saveWord.setOnClickListener {
            if (validateData()) {

                var message: Message
                if(saveWord.text == getString(R.string.update)) {
                    // update word
                    message = Message.obtain(null, WordConstant.UPDATE_WORD)
                    Bundle().apply {
                        val word = Word(wordModel.id ,title, content, System.currentTimeMillis().toString())
                        putParcelable(WordConstant.UPDATE_WORD_OBJECT, word)
                        message.data = this
                        processWordThread.sendMessageToBackgroundThread(message)
                    }

                }else {
                    // save new word
                    message = Message.obtain(null, WordConstant.INSERT_WORD)
                    Bundle().apply {
                        val word = Word(0,title, content, System.currentTimeMillis().toString())
                        putParcelable(WordConstant.INSERT_WORD_OBJECT, word)
                        message.data = this
                        processWordThread.sendMessageToBackgroundThread(message)
                    }
                }
            }
        }
        cancel.setOnClickListener { }
    }

    private fun validateData(): Boolean {
        title = wordTitle.text.toString().trim()
        content = wordContent.text.toString().trim()

        if(title.isNullOrEmpty()) {
            Toast.makeText(this,"Enter Title",Toast.LENGTH_SHORT).show()
            return false
        }else if(content.isNullOrEmpty()) {
            Toast.makeText(this,"Enter Content",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        processWordThread = ProcessWordThread(mainThreadhandler, application)
        processWordThread.start()
    }

    override fun handleMessage(msg: Message?): Boolean {
        when(msg?.what) {
            WordConstant.INSERT_WORD_SUCCESS -> {
                Log.d(TAG, "handleMessage: successfully inserted new word. This is from thread: ${Thread.currentThread().name}")
                processWordThread.quitThread()
                finish()
            }
            WordConstant.INSERT_WORD_FAIL -> {
                Log.d(TAG, "handleMessage: unable to insert new word. This is from thread: ${Thread.currentThread().name}")
                processWordThread.quitThread()
                finish()
            }
            WordConstant.UPDATE_WORD_SUCCESS -> {
                Log.d(TAG, "handleMessage: successfully updated word. This is from thread: ${Thread.currentThread().name}")
                processWordThread.quitThread()
                finish()
            }
            WordConstant.UPDATE_WORD_FAIL -> {
                Log.d(TAG, "handleMessage: unable to delete word. This is from thread: ${Thread.currentThread().name}")
                processWordThread.quitThread()
                finish()
            }
        }
        return false
    }

}
