package com.example.androidlearnings.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidlearnings.R;
import com.example.androidlearnings.activity.EditNoteActivity;
import com.example.androidlearnings.databinding.RecyclerViewRowItemBinding;
import com.example.androidlearnings.room.Note;
import com.example.androidlearnings.utils.Constant;


public class NoteAdapter extends ListAdapter<Note, NoteAdapter.CustomViewHolder> {

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewRowItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recycler_view_row_item, parent, false);
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.CustomViewHolder holder, int position) {
        Note noteData = getItem(position);
        holder.binding.setNote(noteData);
//        holder.binding.tvTitle.setText(noteData.getTitle());
//        holder.binding.tvDescription.setText(noteData.getDescription());
//        holder.binding.tvPriority.setText(String.valueOf(noteData.getPriority()));
    }

    public Note getNoteAt(int pos) {
        return getItem(pos);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private RecyclerViewRowItemBinding binding;

        public CustomViewHolder(@NonNull RecyclerViewRowItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

            itemView.getRoot().setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.NOTE, getItem(getAdapterPosition()));
                bundle.putInt(Constant.NOTE_ID, getItem(getAdapterPosition()).getId());
                Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
                intent.putExtras(bundle);
                ((Activity) v.getContext()).startActivityForResult(intent, Constant.EDIT_NOTE_REQUEST_CODE);
            });
        }
    }
}
