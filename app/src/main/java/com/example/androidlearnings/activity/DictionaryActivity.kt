package com.example.androidlearnings.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlearnings.R
import com.example.androidlearnings.adapter.WordAdapter
import com.example.androidlearnings.room.Word
import com.example.androidlearnings.threading.ProcessWordThread
import com.example.androidlearnings.utils.Constant
import com.example.androidlearnings.utils.WordConstant
import kotlinx.android.synthetic.main.activity_dictonary.*

const val TAG = "DictionaryActivity"

class DictionaryActivity : AppCompatActivity(), Handler.Callback {

    private lateinit var processWordThread: ProcessWordThread
    private lateinit var mainThreadhandler: Handler
    private lateinit var adapter: WordAdapter
    private lateinit var wordList: ArrayList<Word>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictonary)

        mainThreadhandler = Handler(this)
        adapter = WordAdapter(diffUtil)

        floatingActionButton.setOnClickListener {
            startActivityForResult(Intent(this, AddWordActivity::class.java), Constant.ADD_WORD_REQUEST_CODE)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteWord(adapter.getWordAt(viewHolder.adapterPosition))
            }
        }).attachToRecyclerView(wordListRecyclerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK)
            return
    }

    override fun onStart() {
        super.onStart()
        processWordThread = ProcessWordThread(mainThreadhandler, application)
        processWordThread.start()
    }

    private fun deleteWord(word: Word) {
        Log.d(TAG, "sendMessage: sending message from: ${Thread.currentThread().name} thread")

        wordList.remove(word)
        adapter.notifyDataSetChanged()

        val msg = Message.obtain(null, WordConstant.DELETE_WORD)
        Bundle().apply {
            putParcelable(WordConstant.DELETE_WORD_OBJECT, word)
            msg.data = this
        }
        processWordThread.sendMessageToBackgroundThread(msg)
    }

    private fun getWords() {
        Log.d(TAG, "sendMessage: sending message from: ${Thread.currentThread().name} thread")
        val msg = Message.obtain(null, WordConstant.RETRIEVE_WORD)
        processWordThread.sendMessageToBackgroundThread(msg)
    }

    override fun onResume() {
        super.onResume()
        getWords()
    }

    private var diffUtil = object : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title
                    && oldItem.content == newItem.content && oldItem.timestamp == newItem.timestamp
        }

    }

    override fun handleMessage(msg: Message): Boolean {

        when(msg.what) {
            WordConstant.DELETE_WORD_SUCCESS -> {
                Log.d(TAG, "handleMessage: successfully delete word. This is from thread: ${Thread.currentThread().name}")

//                wordListRecyclerView.adapter.
//                getWords()
            }
            WordConstant.DELETE_WORD_FAIL -> {
                Log.d(TAG, "handleMessage: unable to delete word. This is from thread: ${Thread.currentThread().name}")
            }
            WordConstant.RETRIEVE_WORD_SUCCESS -> {
                Log.d(TAG, "handleMessage: successfully retrieved word. This is from thread: ${Thread.currentThread().name}")

                var bundle: Bundle = msg.data

                wordList = bundle.getParcelableArrayList<Word>(WordConstant.WORD_LIST)

                wordList?.let {
                    wordListRecyclerView.adapter = adapter
                    adapter.submitList(wordList)
                }
            }
            WordConstant.RETRIEVE_WORD_FAIL -> {
                Log.d(TAG, "handleMessage: unable to retrieve word. This is from thread: ${Thread.currentThread().name}")
            }
        }
        return false
    }

    override fun onStop() {
        super.onStop()
        processWordThread.quitThread()
    }

}
