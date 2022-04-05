package com.example.notesapp.repositories;

import android.app.Application;

import com.example.notesapp.database.NoteDAO;
import com.example.notesapp.database.NoteDatabase;
import com.example.notesapp.model.Note;

import java.util.List;

public class NoteRepository {
    /*
    Area : variable
     */

    private NoteDAO noteDAO;

    /*
    Area : function
     */

    public NoteRepository(Application application) {
        NoteDatabase db = NoteDatabase.getNoteDatabase(application);
        noteDAO = db.noteDAO();
    }

    public List<Note> getNotes() {
        return noteDAO.getNotes();
    }

    public void insertNote(Note note) {
        noteDAO.insertNotes(note);
    }

    public void updateNote(Note note) {
        noteDAO.updateNote(note);
    }

    public void deleteNote(int id) {
        noteDAO.deleteNote(id);
    }
}
