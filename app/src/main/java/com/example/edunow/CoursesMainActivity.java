package com.example.edunow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CoursesMainActivity extends AppCompatActivity {

    private static final String TAG = "CoursesMainActivity";
    private String userId;

    private RecyclerView newCoursesRecyclerView;
    private RecyclerView topMentorsRecyclerView;
    private ProgressBar coursesProgressBar;
    private ProgressBar mentorsProgressBar;

    private CourseAdapter courseAdapter;
    private MentorAdapter mentorAdapter;
    private List<Course> courses;
    private List<Mentor> mentors;

    private DatabaseReference coursesRef;
    private DatabaseReference mentorsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.courses_activity_main);

        // Get user ID
        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        // Initialize Firebase references
        coursesRef = FirebaseDatabase.getInstance().getReference("courses");
        mentorsRef = FirebaseDatabase.getInstance().getReference("mentors");

        // Initialize views
        initializeViews();

        // Setup RecyclerViews
        setupRecyclerViews();

        // Load data from Firebase
        loadCoursesFromFirebase();
        loadMentorsFromFirebase();
    }

    private void initializeViews() {
        newCoursesRecyclerView = findViewById(R.id.recycler_view_new_courses);
        topMentorsRecyclerView = findViewById(R.id.recycler_view_top_mentors);
        coursesProgressBar = findViewById(R.id.courses_progress_bar);
        mentorsProgressBar = findViewById(R.id.mentors_progress_bar);
    }

    private void setupRecyclerViews() {
        // Setup Courses RecyclerView
        courses = new ArrayList<>();
        courseAdapter = new CourseAdapter(this, courses);
        newCoursesRecyclerView.setAdapter(courseAdapter);
        newCoursesRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Setup Mentors RecyclerView
        mentors = new ArrayList<>();
        mentorAdapter = new MentorAdapter(mentors);
        topMentorsRecyclerView.setAdapter(mentorAdapter);
        topMentorsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void loadCoursesFromFirebase() {
        coursesProgressBar.setVisibility(View.VISIBLE);

        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coursesProgressBar.setVisibility(View.GONE);
                courses.clear();

                try {
                    for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                        Course course = courseSnapshot.getValue(Course.class);
                        if (course != null) {
                            courses.add(course);
                            Log.d(TAG, "Loaded course: " + course.getTitle());
                        }
                    }

                    courseAdapter.notifyDataSetChanged();

                    if (courses.isEmpty()) {
                        Toast.makeText(CoursesMainActivity.this,
                                "No courses available. Please add courses in Firebase.",
                                Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "Total courses loaded: " + courses.size());

                } catch (Exception e) {
                    Log.e(TAG, "Error loading courses: " + e.getMessage(), e);
                    Toast.makeText(CoursesMainActivity.this,
                            "Error loading courses: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                coursesProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "Failed to load courses: " + error.getMessage());
                Toast.makeText(CoursesMainActivity.this,
                        "Failed to load courses: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMentorsFromFirebase() {
        mentorsProgressBar.setVisibility(View.VISIBLE);

        mentorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mentorsProgressBar.setVisibility(View.GONE);
                mentors.clear();

                try {
                    for (DataSnapshot mentorSnapshot : snapshot.getChildren()) {
                        Mentor mentor = mentorSnapshot.getValue(Mentor.class);
                        if (mentor != null) {
                            mentors.add(mentor);
                            Log.d(TAG, "Loaded mentor: " + mentor.getName());
                        }
                    }

                    mentorAdapter.notifyDataSetChanged();

                    if (mentors.isEmpty()) {
                        Toast.makeText(CoursesMainActivity.this,
                                "No mentors available yet.",
                                Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "Total mentors loaded: " + mentors.size());

                } catch (Exception e) {
                    Log.e(TAG, "Error loading mentors: " + e.getMessage(), e);
                    Toast.makeText(CoursesMainActivity.this,
                            "Error loading mentors: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mentorsProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "Failed to load mentors: " + error.getMessage());
                Toast.makeText(CoursesMainActivity.this,
                        "Failed to load mentors: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
