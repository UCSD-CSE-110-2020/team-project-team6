package com.example.team_project_team6.model;

import com.example.team_project_team6.model.Walk;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class WalkStaticTest {

    Calendar mockCalendar;
    Calendar usedCalendar;

    @Before
    public void setup() {
        mockCalendar = Calendar.getInstance();
        mockCalendar.set(2020,2,15,6,24,10);
    }

    @Test
    public void convertWalkStartTimeStringToCalendar() {
        String stopWatchString1 = "00:00:00";
        usedCalendar = Walk.convertWalkStartTimeStringToCalendar(mockCalendar, stopWatchString1);

        Assert.assertEquals(0, usedCalendar.get(Calendar.SECOND));
        Assert.assertEquals(0, usedCalendar.get(Calendar.MINUTE));
        Assert.assertEquals(0, usedCalendar.get(Calendar.HOUR));

        String stopWatchString3 = "06:24:10";
        usedCalendar = Walk.convertWalkStartTimeStringToCalendar(mockCalendar, stopWatchString3);
        Assert.assertEquals(10, usedCalendar.get(Calendar.SECOND));
        Assert.assertEquals(24, usedCalendar.get(Calendar.MINUTE));
        Assert.assertEquals(6, usedCalendar.get(Calendar.HOUR));

        String stopWatchString4 = "10:29:14";
        usedCalendar = Walk.convertWalkStartTimeStringToCalendar(mockCalendar, stopWatchString4);
        Assert.assertEquals(14, usedCalendar.get(Calendar.SECOND));
        Assert.assertEquals(29, usedCalendar.get(Calendar.MINUTE));
        Assert.assertEquals(10, usedCalendar.get(Calendar.HOUR));
    }

    @Test
    public void getWalkDuration() {
        String duration1 = Walk.getWalkDuration(mockCalendar, "06:24:10");
        Assert.assertEquals("00:00:00", duration1);

        String duration2 = Walk.getWalkDuration(mockCalendar, "06:24:12");
        Assert.assertEquals("00:00:02", duration2);

        String duration3 = Walk.getWalkDuration(mockCalendar, "06:35:14");
        Assert.assertEquals("00:11:04", duration3);

        String duration4 = Walk.getWalkDuration(mockCalendar, "10:29:14");
        Assert.assertEquals("04:05:04", duration4);
    }

    @Test
    public void getStepDistanceInMiles() {
        int heightInInches = 0;
        Long stepCount = 0l;
        double stepDistance = Walk.getStepDistanceInMiles(heightInInches, stepCount);
        Assert.assertEquals(0, stepDistance, 0);

        heightInInches = 65;
        stepCount = 0l;
        stepDistance = Walk.getStepDistanceInMiles(heightInInches, stepCount);
        Assert.assertEquals(0, stepDistance, 0);

        heightInInches = 65;
        stepCount = 10l;
        stepDistance = Walk.getStepDistanceInMiles(heightInInches, stepCount);
        Assert.assertEquals(getStepDistance(heightInInches, stepCount), stepDistance, 0);
    }

    @Test
    public void parseStepCountStringToLong() {
        String stepCountStr = "0";
        long stepCountLong = Walk.parseStepCountStringToLong(stepCountStr);
        Assert.assertEquals(0l, stepCountLong);

        stepCountStr = "9";
        stepCountLong = Walk.parseStepCountStringToLong(stepCountStr);
        Assert.assertEquals(9l, stepCountLong);

        stepCountStr = "38";
        stepCountLong = Walk.parseStepCountStringToLong(stepCountStr);
        Assert.assertEquals(38l, stepCountLong);

        stepCountStr = null;
        stepCountLong = Walk.parseStepCountStringToLong(stepCountStr);
        Assert.assertEquals(0l, stepCountLong);
    }

    public double getStepDistance(int heightInInches, Long stepCount) {
        return (0.413 * (double) heightInInches) / 12.0 * stepCount / 5280.0;
    }
}