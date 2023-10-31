package com.powerhouse.zeroone;

public class FileModel {
    private String currentDate;
    private String currentTime;
    private String description;
    private String name;
    private String photoUrl;
    private String title;
    private String uid;
    private String videoUrl;

    public FileModel() {}

    public FileModel(String currentDate, String currentTime, String description, String name, String photoUrl, String title, String uid, String videoUrl) {
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.description = description;
        this.name = name;
        this.photoUrl = photoUrl;
        this.title = title;
        this.uid = uid;
        this.videoUrl = videoUrl;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}

