package org.sast.lostfound.model;

import java.io.Serializable;
import java.util.Date;

public class LostItem implements Serializable {
    private int id;
    private String title;
    private Date time;
    private String photoPath;
    private String location;
    private String description;
    private String category;
    private String status;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LostItem() {
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public LostItem(int id, String title, Date time, String description,
                    String location, String category,
                    String status, String photoPath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.location = location;
        this.category = category;
        this.status = status;
        this.photoPath = photoPath;
    }

    public LostItem(String title, Date time, String description,
                    String location, String category,
                    String status, String photoPath) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.location = location;
        this.category = category;
        this.status = status;
        this.photoPath = photoPath;
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
        return photoPath;
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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "LostItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", photoPath='" + photoPath + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}