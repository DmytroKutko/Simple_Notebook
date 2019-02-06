package com.example.user.notebook.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.user.notebook.model.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert
    void insertNote(Note note);

    @Query("SELECT * FROM Note")
    List<Note> getAllNotes();

    @Delete
    void deleteNote(Note note);
}
