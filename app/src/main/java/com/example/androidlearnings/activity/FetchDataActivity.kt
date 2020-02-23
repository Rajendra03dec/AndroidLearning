package com.example.androidlearnings.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlearnings.R
import com.example.androidlearnings.adapter.NoteAdapter
import com.example.androidlearnings.databinding.ActivityFetchDataBinding
import com.example.androidlearnings.room.Note
import com.example.androidlearnings.utils.Constant
import com.example.androidlearnings.utils.Constant.ADD_NOTE_REQUEST_CODE
import com.example.androidlearnings.viewmodel.NoteViewModel

class FetchDataActivity : AppCompatActivity() {

    lateinit var noteViewModel: NoteViewModel
    lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = DataBindingUtil.setContentView<ActivityFetchDataBinding>(this, R.layout.activity_fetch_data)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteAdapter = NoteAdapter()
        binding.rvList.adapter = noteAdapter

        noteViewModel.allNotes.observe(this, Observer { noteAdapter.submitList(it) })

        binding.floatingActionButton.setOnClickListener { startActivityForResult(Intent(this@FetchDataActivity, AddNoteActivity::class.java), Constant.ADD_NOTE_REQUEST_CODE) }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@FetchDataActivity, "Note Delete!", Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(binding.rvList)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK)
            return

        if (requestCode == ADD_NOTE_REQUEST_CODE) {
            if (data != null && data.extras != null) {
                val bundle = data.extras
                val note = bundle!!.getParcelable<Note>(Constant.NOTE)

                if (note != null) {
                    noteViewModel.insert(note)
                    Toast.makeText(this@FetchDataActivity, "Note Added!", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == Constant.EDIT_NOTE_REQUEST_CODE) {
            if (data != null && data.extras != null) {
                val bundle = data.extras
                val note = bundle!!.getParcelable<Note>(Constant.NOTE)
                val noteId = bundle.getInt(Constant.NOTE_ID)

                if (noteId == -1) {
                    return
                }

                if (note != null) {
                    note.id = noteId
                    noteViewModel.update(note)
                    Toast.makeText(this@FetchDataActivity, "Note updated!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this@FetchDataActivity, "Note Not Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteAll -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this@FetchDataActivity, "Delete All Clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}