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
        this.features = new Features();
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
        if(name != null) {
            Log.i("getName from Route", "return: " + name);
        } else {
            Log.e("getName from Route", "value: null");
        }

        return name;
    }

    public void setName(String name) {
        Log.i("setName from Route", "value: " + name);
        this.name = name;
    }

    public Walk getWalk() {
        if(walk != null) {
            Log.i("getWalk from Route", "return: " + walk.toString());
        } else {
            Log.e("getWalk from Route", "value: null");
        }

        return walk;
    }

    public void setWalk(Walk walk) {
        if(walk != null) {
            Log.i("setWalk from Route", "value: " + walk.toString());
        } else {
            Log.i("setWalk from Route", "value: null");
        }
        this.walk = walk;
    }

    public String getStartPoint() {
        if(startPoint != null) {
            Log.i("getStartPoint from Route", "return: " + startPoint);
        } else {
            Log.e("getStartPoint from Route", "value: null");
        }

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
        if(notes != null) {
            Log.i("getNotes from Route", "return: " + notes);
        } else {
            Log.i("getNotes from Route", "value: null");
        }

        return notes;
    }

    public void setNotes(String notes) {
        Log.i("setNotes from Route", "value: " + notes);
        this.notes = notes;
    }

    public Features getFeatures() {
        if(lastStartDate != null) {
            Log.i("getFeatures from Route", "return: " + features.toString());
        } else {
            Log.i("getFeatures from Route", "value: null");
        }

        return features;
    }

    public void setFeatures(Features features) {
        if(features != null) {
            Log.i("setFeatures from Route", "value: " + features.toString());
        } else {
            Log.i("setFeatures from Route", "value: null");
        }
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
