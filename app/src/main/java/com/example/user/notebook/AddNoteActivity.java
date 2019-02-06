package com.example.user.notebook;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.notebook.database.NoteDatabase;
import com.example.user.notebook.model.Note;

public class AddNoteActivity extends AppCompatActivity {

    private EditText etTitle, etBody;
    private Button btnSubmit;
    private Note note;
    private static final String DATABASE_NAME = "notes_db";
    private NoteDatabase noteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        initView();
        initListener();
    }

    private void initListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = etTitle.getText().toString().trim();
                final String body = etBody.getText().toString().trim();
                note = new Note(title, body);

                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            noteDatabase.noteDAO().insertNote(note);
                        }
                    }).start();
                    Toast.makeText(AddNoteActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }

//                addNoteToDatabase(note);
                MainActivity.notes.add(note);
                etTitle.setText("");
                etBody.setText("");
                finish();
            }
        });
    }

    private void addNoteToDatabase(final Note note) {
//        noteDatabase = Room.databaseBuilder(getApplicationContext(),
//                NoteDatabase.class, DATABASE_NAME)
//                .fallbackToDestructiveMigration()
//                .build();

    }

    private void initView() {

        noteDatabase = Room.databaseBuilder(getApplicationContext(),
                NoteDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        etTitle = findViewById(R.id.etTitle);
        etBody = findViewById(R.id.etBody);
        btnSubmit = findViewById(R.id.btnSubmit);
    }
}
