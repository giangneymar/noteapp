package com.example.notesapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;

import com.example.notesapp.model.Note;
import com.example.notesapp.repositories.Repository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private final Repository repository;
    private MediatorLiveData<List<Note>> notes;
    private MediatorLiveData<List<Note>> searchNote;

    public NoteViewModel(Application application) {
        super(application);
        repository = new Repository(application);
    }

    public MediatorLiveData<List<Note>> getLiveNotes() {
        if (notes == null) {
            notes = new MediatorLiveData<>();
        }
        return notes;
    }

    public MediatorLiveData<List<Note>> getLiveSearchNote() {
        if (searchNote == null) {
            searchNote = new MediatorLiveData<>();
        }
        return searchNote;
    }

    public void getNotes() {
        notes = new MediatorLiveData<>();
        notes.postValue(repository.getNotes());
    }

    public void getSearchNote(String key) {
        searchNote = new MediatorLiveData<>();
        searchNote.postValue(repository.getSearchNote(key));
    }

    public void insertNote(Note note) {
        repository.insertNote(note);
    }

    public void updateNote(Note note) {
        repository.updateNote(note);
    }

    public void deleteNote(int id) {
        repository.deleteNote(id);
    }
}
