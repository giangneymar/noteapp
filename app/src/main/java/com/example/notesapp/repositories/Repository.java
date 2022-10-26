package com.example.notesapp.repositories;

import android.app.Application;

import com.example.notesapp.database.DAO;
import com.example.notesapp.database.Database;
import com.example.notesapp.model.Note;

import java.util.List;

public class Repository {

    private final DAO DAO;

    public Repository(Application application) {
        Database db = Database.getNoteDatabase(application);
        DAO = db.dao();
    }

    public List<Note> getNotes() {
        return DAO.getNotes();
    }

    public List<Note> getSearchNote(String key) {
        return DAO.getSearchNote(key);
    }

    public void insertNote(Note note) {
        DAO.insertNotes(note);
    }

    public void updateNote(Note note) {
        DAO.updateNote(note);
    }

    public void deleteNote(int id) {
        DAO.deleteNote(id);
    }
}
