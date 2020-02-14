package com.example.team_project_team6.model;

import android.util.Log;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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
        if (startTime != null) {
            Log.i("getStartTime from Walk", "return: " + startTime);
        } else {
            Log.i("getStartTime from Walk", "return: null");
        }
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        if (startTime != null) {
            Log.i("setStartTime from Walk", "value: " + startTime);
        } else {
            Log.i("setStartTime from Walk", "value: null");
        }
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

    public static Calendar getWalkStartTimeInCalendar(String stopWatchString) {
        Log.d("MockWalkFragment parseStopWatchTime on: ", stopWatchString);
        String zone = "PST";
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("PST"));

        int month = now.get(Calendar.MONTH);
        int year = now.get(Calendar.YEAR);
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        int ms = now.get(Calendar.MILLISECOND);
        int hour = 0;
        int minute = 0;
        int second = 0;

        String[] timeValues = stopWatchString.split(":");

        if(timeValues.length != 3) {
            Log.e("parseStopWatchTime incorrect size: ", "" + timeValues.length);
        } else {
            hour = Integer.parseInt(timeValues[0]);
            minute = Integer.parseInt(timeValues[1]);
            second = Integer.parseInt(timeValues[2]);
        }

        Calendar newCal = Calendar.getInstance();
        newCal.set(Calendar.YEAR, year);
        newCal.set(Calendar.MONTH, month);
        newCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        newCal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        newCal.set(Calendar.MILLISECOND, ms);
        newCal.set(Calendar.HOUR_OF_DAY, hour);
        newCal.set(Calendar.MINUTE, minute);
        newCal.set(Calendar.SECOND, second);

        return newCal;
    }

    public static String getWalkDuration(Calendar startTimeCal, String endTimeString) {
        Calendar endTimeCal = getWalkStartTimeInCalendar(endTimeString);

        int hours = endTimeCal.get(Calendar.HOUR_OF_DAY) - startTimeCal.get(Calendar.HOUR_OF_DAY);
        int minutes = endTimeCal.get(Calendar.MINUTE) - startTimeCal.get(Calendar.MINUTE);
        int seconds = endTimeCal.get(Calendar.SECOND) - startTimeCal.get(Calendar.SECOND);

        StringBuilder sb = new StringBuilder();
        sb.append(hours);
        sb.append(':');
        sb.append(minutes);
        sb.append(':');
        sb.append(seconds);

        return sb.toString();
    }

    public static double getStepDistanceInMiles(int heightInInches, Long stepCount) {
        double strideDistInFt = (0.413 * (double) heightInInches) / 12.0;
        return (strideDistInFt * (double) stepCount) / 5280.0;
    }

    public static Long parseStepCountStringToLong(String stepCountString) {
        Log.d("parseStepCountStringToLong", "Cast step count from String to Long");
        Long stepCountLong = 0l;
        if(stepCountString != null) {
            stepCountLong = Long.parseLong(stepCountString);
        }
        return stepCountLong;
    }

    @Override
    public String toString() {
        if (startTime != null) {
            return String.format(Locale.ENGLISH, "Steps: %d, Distance: %f, Start Time: %s, Duration: %s",
                    step, dist, startTime, duration);
        } else {
            return String.format(Locale.ENGLISH, "Steps: %d, Distance: %f, Start Time: null, Duration: %s",
                    step, dist, duration);
        }
    }
}
