package com.example.notesapp.repositories;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.example.notesapp.database.NoteDAO;
import com.example.notesapp.model.Note;

import java.util.ArrayList;

public class NoteRepository {
    private final NoteDAO noteDAO;

    public NoteRepository(){
        noteDAO = new NoteDAO();
    }

    public void getNotes(Context context, MutableLiveData<ArrayList<Note>> notes){
        ArrayList<Note> notesTemp = noteDAO.getNotes(context);
        if(notesTemp.size() > 0){
            notes.setValue(notesTemp);
        }
        else {
            notes.setValue(null);
        }
    }
}
