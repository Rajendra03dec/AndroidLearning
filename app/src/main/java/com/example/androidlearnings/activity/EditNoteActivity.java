package com.example.androidlearnings.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.androidlearnings.R;
import com.example.androidlearnings.databinding.ActivityEditNoteBinding;
import com.example.androidlearnings.room.Note;
import com.example.androidlearnings.utils.Constant;

public class EditNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private String title, description;
    private int priority;
    private int noteId;
    private ActivityEditNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_note);

        binding.numberPicker.setMinValue(1);
        //Specify the maximum value/number of NumberPicker
        binding.numberPicker.setMaxValue(10);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        binding.numberPicker.setWrapSelectorWheel(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            Note note = bundle.getParcelable(Constant.NOTE);
            if (note != null) {
                noteId = note.getId();
                binding.etTitle.setText(note.getTitle());
                binding.etDescription.setText(note.getDescription());
                binding.numberPicker.setValue(note.getPriority());
            }
        }

        binding.btnCancel.setOnClickListener(this);
//        binding.btnUpdate.setOnClickListener(this);

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnUpdate:
                if (validate()) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.NOTE_ID, noteId);
                    bundle.putParcelable(Constant.NOTE, new Note(title, description, priority));
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    private boolean validate() {
        title = binding.etTitle.getText().toString().trim();
        description = binding.etDescription.getText().toString().trim();
        priority = binding.numberPicker.getValue();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            Toast.makeText(EditNoteActivity.this, "Enter title and description", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
