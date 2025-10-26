package com.example.edunow;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CourseDetailActivity extends AppCompatActivity {

    private ImageView courseImage;
    private TextView titleText;
    private TextView durationText;
    private TextView descriptionText;
    private TextView syllabusText;
    private Button enrollButton;
    private ProgressBar progressBar;

    private String courseId;
    private String courseTitle;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Get user ID
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        // Initialize views
        courseImage = findViewById(R.id.detail_course_image);
        titleText = findViewById(R.id.detail_course_title);
        durationText = findViewById(R.id.detail_course_duration);
        descriptionText = findViewById(R.id.detail_course_description);
        syllabusText = findViewById(R.id.detail_syllabus_content);
        enrollButton = findViewById(R.id.enroll_button);
        progressBar = findViewById(R.id.enroll_progress_bar);

        // Get data from Intent
        Intent intent = getIntent();
        courseId = intent.getStringExtra("COURSE_ID");
        courseTitle = intent.getStringExtra("COURSE_TITLE");
        String duration = intent.getStringExtra("COURSE_DURATION");
        String description = intent.getStringExtra("COURSE_DESCRIPTION");
        String syllabus = intent.getStringExtra("COURSE_SYLLABUS");
        int imageResId = intent.getIntExtra("COURSE_IMAGE_RES_ID", R.drawable.ux_design_placeholder);

        // Set data to views
        titleText.setText(courseTitle != null ? courseTitle : "Course Title");
        durationText.setText(duration != null ? duration : "Duration not available");
        descriptionText.setText(description != null ? description : "No description available");
        syllabusText.setText(syllabus != null ? syllabus : "Syllabus coming soon");
        courseImage.setImageResource(imageResId);

        // Setup enroll button
        enrollButton.setOnClickListener(v -> enrollInCourse());
    }

    private void enrollInCourse() {
        if (userId == null) {
            Toast.makeText(this, "Please login to enroll", Toast.LENGTH_SHORT).show();
            return;
        }

        if (courseId == null) {
            Toast.makeText(this, "Invalid course", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        enrollButton.setEnabled(false);

        // Save enrollment to Firebase
        DatabaseReference userCoursesRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("enrolledCourses")
                .child(courseId);

        // Create enrolled course object
        User.EnrolledCourse enrolledCourse = new User.EnrolledCourse(
                courseId,
                courseTitle,
                0,  // Initial progress is 0%
                System.currentTimeMillis()
        );

        userCoursesRef.setValue(enrolledCourse)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    enrollButton.setEnabled(true);
                    Toast.makeText(this, "Successfully enrolled in " + courseTitle,
                            Toast.LENGTH_SHORT).show();
                    finish();  // Go back to courses list
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    enrollButton.setEnabled(true);
                    Toast.makeText(this, "Failed to enroll: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
}
