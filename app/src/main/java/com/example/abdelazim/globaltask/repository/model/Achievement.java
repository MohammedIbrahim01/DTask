package com.example.abdelazim.globaltask.repository.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Achievement {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int day;
    private long time;

    @Ignore
    public Achievement(String title, String description, int day, long time) {
        this.title = title;
        this.description = description;
        this.day = day;
        this.time = time;
    }

    public Achievement(int id, String title, String description, int day, long time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.day = day;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
