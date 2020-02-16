package com.example.team_project_team6.model;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.example.team_project_team6.ui.walk.WalkViewModel;

import java.util.Locale;

public class StopWatch {
    private long millisecondTime, startTime;
    private int seconds, minutes, hours;
    private Handler handler;

    private WalkViewModel walkViewModel;

    public StopWatch() {
        Log.i("Stopwatch Constructor", "creating new Stopwatch");
        millisecondTime = 0L ;
        startTime = 0L ;
        seconds = 0 ;
        minutes = 0 ;
        handler = new Handler();
    }

    /**
     * reset and run stopwatch for tracking time on current walk
     * @param walkViewModel the viewmodel object for the walk screen
     */
    public void runStopWatch (WalkViewModel walkViewModel) {
        Log.i("runStopWatch inside of StopWatch", "Restart stopwatch");
        resetWatch();
        this.walkViewModel = walkViewModel;
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }

    /**
     * stops running the stopwatch
     */
    public void stopWatch() {
        Log.i("stopWatch inside of StopWatch", "Stop stopwatch");
        handler.removeCallbacks(runnable);
    }

    /**
     * resets the time on the stopwatch
     */
    public void resetWatch() {
        Log.i("resetWatch inside of StopWatch", "Reset stopwatch");
        millisecondTime = 0L ;
        startTime = 0L ;
        seconds = 0 ;
        minutes = 0 ;
    }

    private Runnable runnable = new Runnable() {

        /**
         * parses milliseconds into a time display for stopwatch and gives the updated time
         */
        public void run() {

            millisecondTime = SystemClock.uptimeMillis() - startTime;


            seconds = (int) (millisecondTime / 1000);

            minutes = seconds / 60;

            hours = minutes / 60;

            seconds = seconds % 60;

            walkViewModel.updateStopWatch(String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds));

            handler.postDelayed(this, 0);

        }

    };
}
