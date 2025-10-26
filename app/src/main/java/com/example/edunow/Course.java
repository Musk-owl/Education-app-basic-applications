package com.example.edunow;

public class Course {
    private String courseId;
    private String title;
    private String duration;
    private String description;
    private String syllabus;
    private String category;
    private String difficulty;
    private String imageUrl;
    private int imageResourceId;  // For local images

    // Default constructor required for Firebase
    public Course() {
    }

    // Constructor for local images
    public Course(String courseId, String title, String duration, int imageResourceId) {
        this.courseId = courseId;
        this.title = title;
        this.duration = duration;
        this.imageResourceId = imageResourceId;
    }

    // Full constructor
    public Course(String courseId, String title, String duration, String description,
                  String syllabus, String category, String difficulty) {
        this.courseId = courseId;
        this.title = title;
        this.duration = duration;
        this.description = description;
        this.syllabus = syllabus;
        this.category = category;
        this.difficulty = difficulty;
    }

    // Getters and Setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSyllabus() { return syllabus; }
    public void setSyllabus(String syllabus) { this.syllabus = syllabus; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getImageResourceId() { return imageResourceId; }
    public void setImageResourceId(int imageResourceId) { this.imageResourceId = imageResourceId; }
}
