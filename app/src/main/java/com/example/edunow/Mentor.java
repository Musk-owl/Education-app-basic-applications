package com.example.edunow;

import java.util.List;

public class Mentor {
    private String mentorId;
    private String name;
    private String title;
    private int coursesCount;
    private String avatarUrl;
    private int avatarResourceId;  // For local images
    private List<String> expertise;

    // Default constructor required for Firebase
    public Mentor() {
    }

    // Constructor for local images
    public Mentor(String name, String title, int coursesCount, int avatarResourceId) {
        this.name = name;
        this.title = title;
        this.coursesCount = coursesCount;
        this.avatarResourceId = avatarResourceId;
    }

    // Full constructor
    public Mentor(String mentorId, String name, String title, int coursesCount,
                  List<String> expertise) {
        this.mentorId = mentorId;
        this.name = name;
        this.title = title;
        this.coursesCount = coursesCount;
        this.expertise = expertise;
    }

    // Getters and Setters
    public String getMentorId() { return mentorId; }
    public void setMentorId(String mentorId) { this.mentorId = mentorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getCoursesCount() { return coursesCount; }
    public void setCoursesCount(int coursesCount) { this.coursesCount = coursesCount; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public int getAvatarResourceId() { return avatarResourceId; }
    public void setAvatarResourceId(int avatarResourceId) { this.avatarResourceId = avatarResourceId; }

    public List<String> getExpertise() { return expertise; }
    public void setExpertise(List<String> expertise) { this.expertise = expertise; }
}
