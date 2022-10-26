package com.example.notesapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notesapp.model.Note;

import java.util.List;

@Dao
public interface DAO {
    @Insert
    void insertNotes(Note... note);

    @Update
    void updateNote(Note note);

    @Query("DELETE FROM Note WHERE id = :id")
    void deleteNote(int id);

    @Query("SELECT * FROM Note")
    List<Note> getNotes();

    @Query("SELECT * FROM Note WHERE title LIKE '%' || :key || '%'")
    List<Note> getSearchNote(String key);

}
