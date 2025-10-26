// MusicTrack.java
package com.example.edunow;

public class MusicTrack {
    private String title;
    private int resourceId;
    private String duration;

    public MusicTrack(String title, int resourceId, String duration) {
        this.title = title;
        this.resourceId = resourceId;
        this.duration = duration;
    }

    public String getTitle() { return title; }
    public int getResourceId() { return resourceId; }
    public String getDuration() { return duration; }
}
