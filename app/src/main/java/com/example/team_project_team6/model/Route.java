package com.example.team_project_team6.model;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Locale;

public class Route {
    private Walk walk;
    private String startPoint;
    private Calendar lastStartDate;
    private String notes;
    private Features features;
    private String name;


    public Route() {
        this.walk = null;
        this.startPoint = "";
        this.lastStartDate = null;
        this.notes = "";
        this.features = null;
        this.name = "";
    }

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
        Log.i("getName from Route", "return: " + name);
        return name;
    }

    public void setName(String name) {
        Log.i("setName from Route", "value: " + name);
        this.name = name;
    }

    public Walk getWalk() {
        Log.i("getWalk from Route", "return: " + walk.toString());
        return walk;
    }

    public void setWalk(Walk walk) {
        Log.i("setWalk from Route", "value: " + walk.toString());
        this.walk = walk;
    }

    public String getStartPoint() {
        Log.i("getStartPoint from Route", "return: " + startPoint);
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        Log.i("setStartPoint from Route", "value: " + startPoint);
        this.startPoint = startPoint;
    }

    public Calendar getLastStartDate() {
        if (lastStartDate == null) {
            Log.i("getLastStartDate from Route", "return: null");
        } else {
            Log.i("getLastStartDate from Route", "return: " + lastStartDate.toString());
        }

        return lastStartDate;
    }

    public void setLastStartDate(@Nullable Calendar lastStartDate) {
        if (lastStartDate != null) {
            Log.i("setLastStartDate from Route", "value: " + lastStartDate.toString());
        } else {
            Log.i("setLastStartDate from Route", "value: null");
        }

        this.lastStartDate = lastStartDate;
    }

    public String getNotes() {
        Log.i("getNotes from Route", "return: " + notes);
        return notes;
    }

    public void setNotes(String notes) {
        Log.i("setNotes from Route", "value: " + notes);
        this.notes = notes;
    }

    public Features getFeatures() {
        Log.i("getFeatures from Route", "return: " + features.toString());
        return features;
    }

    public void setFeatures(Features features) {
        Log.i("setFeatures from Route", "value: " + features.toString());
        this.features = features;
    }

    @Override
    public String toString() {
        if (lastStartDate != null) {
            return String.format(Locale.ENGLISH, "Name: %s, Walk: (%s), Starting Point: %s, Last Start Date: %s, Notes: %s,  Features: %s",
                    name, walk.toString(), startPoint, lastStartDate.toString(), notes, features.toString());
        } else {
            return String.format(Locale.ENGLISH, "Name: %s, Walk: (%s), Starting Point: %s, Last Start Date: null, Notes: %s,  Features: %s",
                    name, walk.toString(), startPoint, notes, features.toString());
        }
    }
}
