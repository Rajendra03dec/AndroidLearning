package com.example.androidlearnings.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.androidlearnings.room.LearningDatabase;
import com.example.androidlearnings.room.Note;
import com.example.androidlearnings.room.NoteDao;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        LearningDatabase learningDatabase = LearningDatabase.getInstance(application);
        noteDao = learningDatabase.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note) {
        new InsertNoteAsynTask(noteDao).execute(note);
    }

    public void update(Note note) {
        new UpdateNoteAsynTask(noteDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteNoteAsynTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsynTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes()
    {
        return allNotes;
    }

    private static class InsertNoteAsynTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        InsertNoteAsynTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsynTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        UpdateNoteAsynTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsynTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        DeleteNoteAsynTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsynTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        DeleteAllNotesAsynTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
