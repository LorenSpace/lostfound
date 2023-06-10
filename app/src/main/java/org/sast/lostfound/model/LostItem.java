package org.sast.lostfound.model;

import java.io.Serializable;
import java.util.Date;

public class LostItem implements Serializable {
    private final String title;
    private Date time;
    private final String location;
    private final String description;
    private final String category;
    private String status;
    private final String PhotoPath;

    public LostItem(String title, Date time,
                    String location, String category,
                    String status, String photoPath, String description) {
        this.title = title;
        this.time = time;
        this.location = location;
        this.category = category;
        this.status = status;
        this.PhotoPath = photoPath;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public Date getTime() {
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

    public String getPhotoPath() {
        return PhotoPath;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}