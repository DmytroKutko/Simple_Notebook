package com.example.user.notebook;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.user.notebook.adapter.NoteAdapter;
import com.example.user.notebook.database.NoteDatabase;
import com.example.user.notebook.dialog.NoteDialog;
import com.example.user.notebook.model.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Note> notes;
    Spinner spinner;
    String[] items;
    RecyclerView rvNotes;
    NoteAdapter adapter;
    RecyclerView.LayoutManager manager;
    FloatingActionButton fabAdd;
    private static final String DATABASE_NAME = "notes_db";
    private NoteDatabase noteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView() {
        notes = new ArrayList<>();

        initDatabase();
        initSpinner();
        initRecyclerView();

        fabAdd = findViewById(R.id.fabAdd);
    }

    private void initListener() {
        spinnerListener();
        fabAddListener();
        adapterClickListener();
    }

    private void adapterClickListener() {
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                openDialog(position);
            }

            @Override
            public void onDeleteClick(int position) {
                Note note = notes.get(position);
                deleteItem(position, note);
            }
        });
    }

    private void deleteItem(final int position, final Note note) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                noteDatabase.noteDAO().deleteNote(note);
            }
        }).start();
        notes.remove(position);
        adapter.notifyItemRemoved(position);
    }

    private void openDialog(int position) {
        NoteDialog dialog = new NoteDialog(notes.get(position).getUnixTime());
        dialog.show(getSupportFragmentManager(), "note dialog");
    }

    private void initDatabase() {
        noteDatabase = Room.databaseBuilder(getApplicationContext(),
                NoteDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
        // Get all notes from database
        new Thread(new Runnable() {
            @Override
            public void run() {
                notes.addAll(noteDatabase.noteDAO().getAllNotes());
            }
        }).start();
    }

    private void fabAddListener() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void spinnerListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (notes.size() > 0){

                    switch (items[position]){
                        case "Title":
                            Collections.sort(notes, new Comparator<Note>() {
                                @Override
                                public int compare(Note o1, Note o2) {
                                    return o1.getTitle().compareTo(o2.getTitle());
                                }
                            });
                            adapter.notifyDataSetChanged();
                            break;

                            case "Body":
                            Collections.sort(notes, new Comparator<Note>() {
                                @Override
                                public int compare(Note o1, Note o2) {
                                    return o1.getBody().compareTo(o2.getBody());
                                }
                            });
                            adapter.notifyDataSetChanged();
                            break;

                            case "Time":
                            Collections.sort(notes, new Comparator<Note>() {
                                @Override
                                public int compare(Note o1, Note o2) {
                                    return Double.compare(o1.getUnixTime(), o2.getUnixTime());
                                }
                            });
                            adapter.notifyDataSetChanged();
                            break;

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecyclerView() {
        rvNotes = findViewById(R.id.rvNotes);
        rvNotes.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        adapter = new NoteAdapter(notes);
        rvNotes.setLayoutManager(manager);
        rvNotes.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initSpinner() {
        spinner = findViewById(R.id.spinner);
        items = new String[]{"Title", "Body", "Time"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
