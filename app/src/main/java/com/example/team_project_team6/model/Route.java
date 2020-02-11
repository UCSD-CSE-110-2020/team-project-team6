package com.example.team_project_team6.model;

import java.util.Calendar;

public class Route {
    Walk walk;
    String startPoint;
    Calendar lastStartDate;
    String notes;
    Features features;
    String name;

    // If never walked, set lastStartDate to null
    public Route(Walk walk, String startPoint, Calendar lastStartDate, String notes, Features features, String name) {
        this.walk = walk;
        this.startPoint = startPoint;
        this.lastStartDate = lastStartDate;
        this.notes = notes;
        this.features = features;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Walk getWalk() {
        return walk;
    }

    public void setWalk(Walk walk) {
        this.walk = walk;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public Calendar getLastStartDate() {
        return lastStartDate;
    }

    public void setLastStartDate(Calendar lastStartDate) {
        this.lastStartDate = lastStartDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Features getFeatures() {
        return features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }
}
