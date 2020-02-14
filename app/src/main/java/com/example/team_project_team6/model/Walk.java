package com.example.team_project_team6.model;

import android.util.Log;

import java.util.Calendar;
import java.util.Locale;

public class Walk {
    private String duration;
    private Calendar startTime;
    private double dist;
    private long step;

    public Walk () {
        startTime = Calendar.getInstance();
        duration = "";
        dist = 0;
        step = 0;
    }

    public Walk (Calendar startTime) {
        this.setStartTime(startTime);
    }

    public String getDuration() {
        Log.i("getDuration from Walk", "return: " + duration);
        return duration;
    }

    public void setDuration(String duration) {
        Log.i("setDuration from Walk", "value: " + duration);
        this.duration = duration;
    }

    public Calendar getStartTime() {
        Log.i("getStartTime from Walk", "return: " + startTime);
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        Log.i("setStartTime from Walk", "value: " + startTime);
        this.startTime = startTime;
    }

    public double getDist() {
        Log.i("getDist from Walk", "return: " + dist);

        return dist;
    }

    public void setDist(double dist) {
        Log.i("setDist from Walk", "value: " + dist);
        this.dist = dist;
    }

    public long getStep() {
        Log.i("getStep from Walk", "return: " + step);
        return step;
    }

    public void setStep(long step) {
        Log.i("setStep from Walk", "value: " + step);
        this.step = step;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Steps: %d, Distance: %f, Start Time: %s, Duration: %s",
                step, dist, startTime, duration);
    }
}
