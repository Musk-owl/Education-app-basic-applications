package com.example.edunow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CourseContentActivity extends AppCompatActivity {

    private TextView courseTitleText;
    private TextView progressText;
    private SeekBar progressSeekBar;
    private Button updateProgressButton;
    private Button backButton;
    private ProgressBar loadingProgressBar;

    private String courseId;
    private String courseTitle;
    private int currentProgress;
    private String userId;
    private DatabaseReference userCourseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);

        // Initialize Firebase
        userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        // Get data from Intent
        courseId = getIntent().getStringExtra("COURSE_ID");
        courseTitle = getIntent().getStringExtra("COURSE_TITLE");
        currentProgress = getIntent().getIntExtra("PROGRESS", 0);

        if (userId != null) {
            userCourseRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .child("enrolledCourses")
                    .child(courseId);
        }

        // Initialize views
        initializeViews();

        // Setup UI with course data
        setupCourseInfo();

        // Setup listeners
        setupListeners();
    }

    private void initializeViews() {
        courseTitleText = findViewById(R.id.course_content_title);
        progressText = findViewById(R.id.course_progress_text);
        progressSeekBar = findViewById(R.id.course_progress_seekbar);
        updateProgressButton = findViewById(R.id.update_progress_button);
        backButton = findViewById(R.id.back_to_dashboard_button);
        loadingProgressBar = findViewById(R.id.content_loading_progress);
    }

    private void setupCourseInfo() {
        courseTitleText.setText(courseTitle);
        progressText.setText("Current Progress: " + currentProgress + "%");
        progressSeekBar.setProgress(currentProgress);
        progressSeekBar.setMax(100);
    }

    private void setupListeners() {
        // Update progress text as user moves the seekbar
        progressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressText.setText("Current Progress: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Update progress button
        updateProgressButton.setOnClickListener(v -> updateProgress());

        // Back button
        backButton.setOnClickListener(v -> finish());
    }

    private void updateProgress() {
        if (userCourseRef == null) {
            Toast.makeText(this, "Please login to update progress", Toast.LENGTH_SHORT).show();
            return;
        }

        int newProgress = progressSeekBar.getProgress();

        loadingProgressBar.setVisibility(View.VISIBLE);
        updateProgressButton.setEnabled(false);

        // Update progress in Firebase
        userCourseRef.child("progress").setValue(newProgress)
                .addOnSuccessListener(aVoid -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    updateProgressButton.setEnabled(true);
                    currentProgress = newProgress;
                    Toast.makeText(CourseContentActivity.this,
                            "Progress updated to " + newProgress + "%",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    updateProgressButton.setEnabled(true);
                    Toast.makeText(CourseContentActivity.this,
                            "Failed to update progress: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}
