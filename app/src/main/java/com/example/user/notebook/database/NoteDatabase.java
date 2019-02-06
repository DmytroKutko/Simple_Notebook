package com.example.user.notebook.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.user.notebook.dao.NoteDAO;
import com.example.user.notebook.model.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDAO noteDAO();
}
