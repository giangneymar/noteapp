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

    /*
    Area : function
     */
    public abstract NoteDAO noteDAO();

    public static synchronized NoteDatabase getNoteDatabase(Context context) {
        if (noteDatabase == null) {
            noteDatabase = Room.databaseBuilder(context, NoteDatabase.class, "Note")
                    .allowMainThreadQueries()
                    .build();
        }
        return noteDatabase;
    }
}
