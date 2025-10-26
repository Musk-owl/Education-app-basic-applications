package com.example.edunow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;

public class AddNoteDialog extends Dialog {

    private EditText titleInput;
    private EditText contentInput;
    private EditText courseIdInput;
    private Button saveButton;
    private Button cancelButton;
    private OnNoteSavedListener listener;
    private User.Note existingNote;

    public interface OnNoteSavedListener {
        void onNoteSaved(String title, String content, String courseId);
    }

    public AddNoteDialog(@NonNull Context context, OnNoteSavedListener listener) {
        super(context);
        this.listener = listener;
    }

    public AddNoteDialog(@NonNull Context context, User.Note note, OnNoteSavedListener listener) {
        super(context);
        this.existingNote = note;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_note);

        titleInput = findViewById(R.id.note_title_input);
        contentInput = findViewById(R.id.note_content_input);
        courseIdInput = findViewById(R.id.note_course_input);
        saveButton = findViewById(R.id.save_note_button);
        cancelButton = findViewById(R.id.cancel_note_button);

        if (existingNote != null) {
            titleInput.setText(existingNote.getTitle());
            contentInput.setText(existingNote.getContent());
            courseIdInput.setText(existingNote.getCourseId());
        }

        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String content = contentInput.getText().toString().trim();
            String courseId = courseIdInput.getText().toString().trim();

            if (!title.isEmpty() && !content.isEmpty()) {
                listener.onNoteSaved(title, content, courseId);
                dismiss();
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }
}
