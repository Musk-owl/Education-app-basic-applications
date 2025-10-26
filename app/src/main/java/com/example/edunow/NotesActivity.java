package com.example.edunow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.*;

public class NotesActivity extends AppCompatActivity {
    private String userId;
    private EditText noteContent;
    private Button saveNoteBtn;
    private ProgressBar notesProgressBar;
    private RecyclerView notesRecyclerView;
    private NotesAdapter notesAdapter;
    private List<User.Note> notesList = new ArrayList<>();
    private DatabaseReference notesRef;
    private User.Note editingNote = null; // Track currently editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        noteContent = findViewById(R.id.edit_note_content);
        saveNoteBtn = findViewById(R.id.btn_save_note);
        notesProgressBar = findViewById(R.id.notes_progress_bar);
        notesRecyclerView = findViewById(R.id.notes_recycler_view);

        notesAdapter = new NotesAdapter(
                notesList,
                this::onEditNote,
                this::onDeleteNote
        );
        notesRecyclerView.setAdapter(notesAdapter);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        notesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("notes");

        saveNoteBtn.setOnClickListener(v -> saveNote());
        loadNotes();
    }

    private void saveNote() {
        String noteText = noteContent.getText().toString().trim();
        if (noteText.isEmpty()) {
            Toast.makeText(this, "Please enter something", Toast.LENGTH_SHORT).show();
            return;
        }

        notesProgressBar.setVisibility(View.VISIBLE);

        if (editingNote != null) {
            // Update
            notesRef.child(editingNote.getId()).child("content").setValue(noteText)
                    .addOnSuccessListener(aVoid -> {
                        noteContent.setText("");
                        editingNote = null;
                        notesProgressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Add new
            String noteId = notesRef.push().getKey();
            User.Note note = new User.Note(noteId, "", noteText, noteText, System.currentTimeMillis());
            notesRef.child(noteId).setValue(note)
                    .addOnSuccessListener(aVoid -> {
                        noteContent.setText("");
                        notesProgressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void onEditNote(User.Note note) {
        noteContent.setText(note.getContent());
        editingNote = note;
    }

    private void onDeleteNote(User.Note note) {
        notesProgressBar.setVisibility(View.VISIBLE);
        notesRef.child(note.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    notesProgressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadNotes() {
        notesProgressBar.setVisibility(View.VISIBLE);
        notesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notesList.clear();
                for (DataSnapshot n : snapshot.getChildren()) {
                    User.Note note = n.getValue(User.Note.class);
                    if (note != null) notesList.add(note);
                }
                notesAdapter.notifyDataSetChanged();
                notesProgressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                notesProgressBar.setVisibility(View.GONE);
                Toast.makeText(NotesActivity.this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

