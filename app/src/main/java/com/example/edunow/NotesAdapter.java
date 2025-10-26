package com.example.edunow;

import android.view.*;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private final List<User.Note> notes;
    private final OnEditClickListener editListener;
    private final OnDeleteClickListener deleteListener;

    public interface OnEditClickListener { void onEdit(User.Note note); }
    public interface OnDeleteClickListener { void onDelete(User.Note note); }

    public NotesAdapter(List<User.Note> notes, OnEditClickListener editListener, OnDeleteClickListener deleteListener) {
        this.notes = notes;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder h, int pos) {
        User.Note note = notes.get(pos);
        h.noteText.setText(note.getContent());

        h.btnEdit.setOnClickListener(v -> editListener.onEdit(note));
        h.btnDelete.setOnClickListener(v -> deleteListener.onDelete(note));
    }

    @Override
    public int getItemCount() { return notes.size(); }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteText;
        ImageButton btnEdit, btnDelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteText = itemView.findViewById(R.id.text_note_content);
            btnEdit = itemView.findViewById(R.id.item_note_edit);
            btnDelete = itemView.findViewById(R.id.item_note_delete);
        }
    }
}
