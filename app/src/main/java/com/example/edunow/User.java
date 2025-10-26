package com.example.edunow;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String uid;
    private String email;
    private String name;
    private Map<String, EnrolledCourse> enrolledCourses;
    private Map<String, Note> notes;

    public User() {
        // Default constructor required for Firebase
        this.enrolledCourses = new HashMap<>();
        this.notes = new HashMap<>();
    }

    public User(String uid, String email, String name) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.enrolledCourses = new HashMap<>();
        this.notes = new HashMap<>();
    }

    // Getters and Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Map<String, EnrolledCourse> getEnrolledCourses() { return enrolledCourses; }
    public void setEnrolledCourses(Map<String, EnrolledCourse> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public Map<String, Note> getNotes() { return notes; }
    public void setNotes(Map<String, Note> notes) { this.notes = notes; }

    public static class EnrolledCourse {
        private String courseId;
        private String courseTitle;
        private int progress; // 0-100
        private long enrolledDate;

        public EnrolledCourse() {}

        public EnrolledCourse(String courseId, String courseTitle, int progress, long enrolledDate) {
            this.courseId = courseId;
            this.courseTitle = courseTitle;
            this.progress = progress;
            this.enrolledDate = enrolledDate;
        }

        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }

        public String getCourseTitle() { return courseTitle; }
        public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }

        public long getEnrolledDate() { return enrolledDate; }
        public void setEnrolledDate(long enrolledDate) { this.enrolledDate = enrolledDate; }
    }

    public static class Note {
        private String noteId;
        private String courseId;
        private String title;
        private String content;
        private long timestamp;

        public Note() {}

        public Note(String noteId, String courseId, String title, String content, long timestamp) {
            this.noteId = noteId;
            this.courseId = courseId;
            this.title = title;
            this.content = content;
            this.timestamp = timestamp;
        }

        public String getNoteId() { return noteId; }
        public void setNoteId(String noteId) { this.noteId = noteId; }

        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

        public String getId() {
            return noteId;
        }
    }
}
