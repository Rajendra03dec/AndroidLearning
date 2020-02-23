package com.example.androidlearnings.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.androidlearnings.R;
import com.example.androidlearnings.databinding.ActivityNewNoteBinding;
import com.example.androidlearnings.room.Note;
import com.example.androidlearnings.utils.Constant;

public class AddNoteActivity extends AppCompatActivity {

    private String title, description;
    private ActivityNewNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_note);

        binding.numberPicker.setMinValue(1);
        //Specify the maximum value/number of NumberPicker
        binding.numberPicker.setMaxValue(10);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        binding.numberPicker.setWrapSelectorWheel(true);

        binding.numberPicker.setValue(1);

        binding.btnSave.setOnClickListener(v -> {

            if (validate()) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.NOTE, new Note(title, description, binding.numberPicker.getValue()));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        binding.btnCancel.setOnClickListener(view -> finish());
    }

    private boolean validate() {
        title = binding.etTitle.getText().toString().trim();
        description = binding.etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(AddNoteActivity.this, "Enter Title", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(AddNoteActivity.this, "Enter Description", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
