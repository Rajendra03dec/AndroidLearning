package com.example.androidlearnings.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidlearnings.R;
import com.example.androidlearnings.adapter.NoteAdapter;
import com.example.androidlearnings.databinding.ActivityFetchDataBinding;
import com.example.androidlearnings.pojo.TestPojo;
import com.example.androidlearnings.room.Note;
import com.example.androidlearnings.utils.Constant;
import com.example.androidlearnings.viewmodel.NoteViewModel;

public class FetchDataActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFetchDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fetch_data);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        adapter = new NoteAdapter();
        binding.rvList.setAdapter(adapter);

        noteViewModel.getAllNotes().observe(this, notes -> {
            //Update Recycler view
//            rvList.setAdapter(adapter);
            adapter.submitList(notes);
        });

        binding.floatingActionButton.setOnClickListener(v -> startActivityForResult(new Intent(FetchDataActivity.this, AddNoteActivity.class), Constant.ADD_NOTE_REQUEST_CODE));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(FetchDataActivity.this, "Note Delete!", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(binding.rvList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == Constant.ADD_NOTE_REQUEST_CODE) {
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                Note note = bundle.getParcelable(Constant.NOTE);

                if (note != null) {
                    noteViewModel.insert(note);
                    Toast.makeText(FetchDataActivity.this, "Note Added!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == Constant.EDIT_NOTE_REQUEST_CODE) {
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                Note note = bundle.getParcelable(Constant.NOTE);
                int noteId = bundle.getInt(Constant.NOTE_ID);

                if (noteId == -1) {
                    return;
                }

                if (note != null) {
                    note.setId(noteId);
                    noteViewModel.update(note);
                    Toast.makeText(FetchDataActivity.this, "Note updated!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(FetchDataActivity.this, "Note Not Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll:
                noteViewModel.deleteAllNotes();
                Toast.makeText(FetchDataActivity.this, "Delete All Clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
