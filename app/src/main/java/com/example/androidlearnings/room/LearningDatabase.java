package com.example.androidlearnings.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class, Word.class}, version = 1)
public abstract class LearningDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "learning_database";
    private static LearningDatabase dbInstance;

    public abstract NoteDao noteDao();
    public abstract WordDao wordDao();

    public static synchronized LearningDatabase getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(context.getApplicationContext(), LearningDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return dbInstance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsynTask(dbInstance).execute();
        }
    };

    private static class PopulateDbAsynTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        public PopulateDbAsynTask(LearningDatabase learningDatabase) {
            noteDao = learningDatabase.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Description 1", 2));
            noteDao.insert(new Note("Title 2", "Description 2", 8));
            noteDao.insert(new Note("Title 3", "Description 3", 1));
            return null;
        }
    }
}
