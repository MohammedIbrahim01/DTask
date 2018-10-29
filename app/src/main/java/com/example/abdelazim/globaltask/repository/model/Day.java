package com.example.abdelazim.globaltask.repository.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Day {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int day;
    private String header;
    private int doneNum = 0, lateNum = 0;

    @Ignore
    public Day(int day, String header) {
        this.day = day;
        this.header = header;
    }

    public Day(int id, int day, String header) {
        this.id = id;
        this.day = day;
        this.header = header;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getDoneNum() {
        return doneNum;
    }

    public void setDoneNum(int doneNum) {
        this.doneNum = doneNum;
    }

    public int getLateNum() {
        return lateNum;
    }

    public void setLateNum(int lateNum) {
        this.lateNum = lateNum;
    }
}
