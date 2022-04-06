package com.example.notesapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notesapp.model.Note;

@Database(entities = Note.class, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    /*
    Area : variable
     */
    private static NoteDatabase noteDatabase;
    private static String DATABASE_NAME = "Note";

    /*
    Area : function
     */

    public abstract NoteDAO noteDAO();

    public static synchronized NoteDatabase getNoteDatabase(Context context) {
        if (noteDatabase == null) {
            noteDatabase = Room.databaseBuilder(context, NoteDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return noteDatabase;
    }
}
