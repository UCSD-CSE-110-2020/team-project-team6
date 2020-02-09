package com.example.team_project_team6.model;

import java.util.Calendar;

public class Walk {
    Calendar duration;
    Calendar startTime;
    double dist;
    int step;

    Walk () {}

    Walk (Calendar startTime) {
        this.setStartTime(startTime);
    }

    public Calendar getDuration() {
        return duration;
    }

    public void setDuration(Calendar duration) {
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
