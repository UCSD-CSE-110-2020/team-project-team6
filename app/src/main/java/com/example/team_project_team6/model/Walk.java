package com.example.team_project_team6.model;

import java.util.Calendar;

public class Walk {
    String duration;
    Calendar startTime;
    double dist;
    int step;

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
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return String.format("Steps: %d, Distance: %f, Start Time: %s, Duration: %s",
                step, dist, startTime, duration);
    }
}
