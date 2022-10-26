package com.example.notesapp.database;

import static com.example.notesapp.utils.KeyConstant.DATABASE_NAME;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notesapp.model.Note;

@androidx.room.Database(entities = {Note.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database database;

    public abstract DAO dao();

    public static synchronized Database getNoteDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, Database.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }
}
