package com.example.notesapp.database;

import static com.example.notesapp.view.activity.NoteActivity.notes;
import android.content.Context;
import android.database.Cursor;
import com.example.notesapp.model.Note;
import java.util.ArrayList;

public class NoteDAO {
    public void createTableNote(Context context) {
        Database.getInstance(context).QueryData("CREATE TABLE IF NOT EXISTS Note(Id INTEGER PRIMARY KEY AUTOINCREMENT, NoteTitle VARCHAR(255), NoteContent VARCHAR(255), DATE VARCHAR(255))");
    }

    public ArrayList<Note> getNotes(Context context) {
        Cursor dataNote = Database.getInstance(context).getData("SELECT * FROM Note");
        while (dataNote.moveToNext()) {
            int id = dataNote.getInt(0);
            String title = dataNote.getString(1);
            String content = dataNote.getString(2);
            String date = dataNote.getString(3);
            notes.add(new Note(id, title, content, date));
        }
        return notes;
    }

    public void insertNote(Context context, String title, String content, String date) {
        Database.getInstance(context).QueryData("INSERT INTO Note VALUES(null,'" + title + "','" + content + "','" + date + "')");
    }

    public void updateNote(Context context, int id, String newTitle, String newContent, String newDate) {
        Database.getInstance(context).QueryData("UPDATE Note SET NoteTitle = '" + newTitle + "', NoteContent = '" + newContent + "', Date = '" + newDate + "' WHERE Id = " + id + "");
    }

    public void deleteNote(Context context, int id) {
        Database.getInstance(context).QueryData("DELETE FROM Note WHERE Id = " + id + "");
    }
}
