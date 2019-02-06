package com.example.user.notebook.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.notebook.R;
import com.example.user.notebook.model.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> notes;

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        public ImageButton ibDelete;
        public TextView tvTitle, tvBody;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            ibDelete = itemView.findViewById(R.id.ibDelete);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBody = itemView.findViewById(R.id.tvBody);
        }
    }

    public NoteAdapter(ArrayList<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_item, viewGroup, false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {
        Note note = notes.get(i);
        noteViewHolder.tvTitle.setText(note.getTitle());
        noteViewHolder.tvBody.setText(note.getBody());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
