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
import android.widget.Toast;

import com.example.user.notebook.adapter.NoteAdapter;
import com.example.user.notebook.database.NoteDatabase;
import com.example.user.notebook.model.Note;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Note> notes;
    Spinner spinner;
    String[] items;
    RecyclerView rvNotes;
    RecyclerView.Adapter adapter;
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
        noteDatabase = Room.databaseBuilder(getApplicationContext(),
                NoteDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        notes = new ArrayList<>();

        // Add all notes from database
        new Thread(new Runnable() {
            @Override
            public void run() {
                notes.addAll(noteDatabase.noteDAO().getAllNotes());
            }
        }).start();


        // Test !!!
        notes.add(new Note("Title1", "Some text"));
        sleep();
        notes.add(new Note("Super Title", "Other text"));
        sleep();
        notes.add(new Note("Title title title", "New note text"));
        // end Test

        initSpinner();
        initRecyclerView();

        fabAdd = findViewById(R.id.fabAdd);
    }

    private void initListener() {
        spinnerListener();
        fabAddListener();
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
                Toast.makeText(MainActivity.this, "Sorted by: " + items[position], Toast.LENGTH_SHORT).show();
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

    //Sleep 10 millis
    private void sleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
