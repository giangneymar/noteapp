package com.example.notesapp.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.database.NoteDAO;
import com.example.notesapp.model.Note;
import com.example.notesapp.repositories.NoteRepository;

import java.util.ArrayList;

public class NoteViewModel extends ViewModel {
    private final NoteRepository noteRepository;
    private final NoteDAO noteDAO;
    private final MutableLiveData<ArrayList<Note>> notesLive;

    public NoteViewModel() {
        noteRepository = new NoteRepository();
        noteDAO = new NoteDAO();
        notesLive = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Note>> getNotesObserver(Context context) {
        getNotes(context);
        return notesLive;
    }

    public void getNotes(Context context){
        noteRepository.getNotes(context, notesLive);
    }

    public void createTableNote(Context context){
        noteDAO.createTableNote(context);
    }

    public void insertNote(Context context, String title, String content, String date) {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setDate(date);
        noteDAO.insertNote(context, note.getTitle(), note.getContent(),note.getDate());
        getNotes(context);
    }

    public void updateNote(Context context, int id, String newTitle, String newContent, String newDate) {
        noteDAO.updateNote(context, id, newTitle, newContent, newDate);
        getNotes(context);
    }

    public void deleteNote(Context context, int id) {
        noteDAO.deleteNote(context, id);
        getNotes(context);
    }
}
