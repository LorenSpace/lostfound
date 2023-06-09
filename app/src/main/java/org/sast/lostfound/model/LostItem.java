package org.sast.lostfound.model;

import android.graphics.Bitmap;

public class LostItem {
    private String title;
    private String time;
    private String location;
    private String category;
    private String status;
    private Bitmap photo;

    public LostItem(String title, String time, String location, String category, String status, Bitmap photo) {
        this.title = title;
        this.time = time;
        this.location = location;
        this.category = category;
        this.status = status;
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public Bitmap getPhoto() {
        return photo;
    }
}