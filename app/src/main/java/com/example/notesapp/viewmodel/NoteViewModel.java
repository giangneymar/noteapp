package com.example.notesapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.notesapp.model.Note;
import com.example.notesapp.repositories.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    /*
    Area : variable
     */

    private final NoteRepository noteRepository;
    public MutableLiveData<List<Note>> data;

    /*
    Area : function
     */

    public NoteViewModel(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
    }

    public MutableLiveData<List<Note>> getNotes() {
        data = new MutableLiveData<>();
        if (noteRepository.getNotes().size() > 0) {
            data.postValue(noteRepository.getNotes());
        } else {
            data.postValue(null);
        }
        return data;
    }

    public void insertNote(Note note) {
        noteRepository.insertNote(note);
    }

    public void updateNote(Note note) {
        noteRepository.updateNote(note);
    }

    public void deleteNote(int id) {
        noteRepository.deleteNote(id);
    }
}
