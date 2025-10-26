package com.example.edunow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;  // ✅ ADD THIS IMPORT
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private String userId;

    private TextView greetingText;
    private RecyclerView enrolledCoursesRecyclerView;
    private ProgressBar progressBar;
    private Button browseCoursesButton;
    private Button notesButton;
    private Button musicButton;
    private ImageButton logoutButton;  // ✅ CHANGED FROM Button TO ImageButton

    private EnrolledCourseAdapter enrolledCourseAdapter;
    private List<User.EnrolledCourse> enrolledCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_dashboard);
            Log.d(TAG, "Dashboard layout loaded");

            // Initialize Firebase
            mAuth = FirebaseAuth.getInstance();
            userId = getIntent().getStringExtra("USER_ID");

            if (userId == null && mAuth.getCurrentUser() != null) {
                userId = mAuth.getCurrentUser().getUid();
            }

            if (userId == null) {
                Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            Log.d(TAG, "Firebase initialized for user: " + userId);

            // Initialize views
            initializeViews();

            // Setup RecyclerView
            setupEnrolledCoursesRecyclerView();

            // Load user data
            loadUserData();

            // Setup button listeners
            setupButtonListeners();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error loading dashboard: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        try {
            greetingText = findViewById(R.id.greetingText);
            enrolledCoursesRecyclerView = findViewById(R.id.enrolled_courses_recycler_view);
            progressBar = findViewById(R.id.dashboard_progress_bar);
            browseCoursesButton = findViewById(R.id.browse_courses_button);
            notesButton = findViewById(R.id.notes_button);
            musicButton = findViewById(R.id.music_button);
            logoutButton = findViewById(R.id.logout_button);  // ✅ Now matches ImageButton type

            Log.d(TAG, "All views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage(), e);
            throw e;
        }
    }

    private void setupEnrolledCoursesRecyclerView() {
        enrolledCourses = new ArrayList<>();
        enrolledCourseAdapter = new EnrolledCourseAdapter(this, enrolledCourses);
        enrolledCoursesRecyclerView.setAdapter(enrolledCourseAdapter);
        enrolledCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadUserData() {
        progressBar.setVisibility(View.VISIBLE);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);

                try {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            String name = user.getName() != null ? user.getName() : "Student";
                            greetingText.setText("Welcome, " + name + " 👋");

                            // Load enrolled courses
                            enrolledCourses.clear();
                            if (user.getEnrolledCourses() != null) {
                                enrolledCourses.addAll(user.getEnrolledCourses().values());
                            }
                            enrolledCourseAdapter.notifyDataSetChanged();

                            Log.d(TAG, "User data loaded successfully");
                        }
                    } else {
                        Log.d(TAG, "User data doesn't exist yet");
                        greetingText.setText("Welcome, Student 👋");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error loading user data: " + e.getMessage(), e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Database error: " + error.getMessage());
                Toast.makeText(DashboardActivity.this,
                        "Failed to load data: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtonListeners() {
        browseCoursesButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, CoursesMainActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        notesButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, NotesActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        musicButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MusicPlayerActivity.class);
            startActivity(intent);
        });


        logoutButton.setOnClickListener(v -> logout());
    }


    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
