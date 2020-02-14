package com.example.team_project_team6;

import android.os.Handler;
import android.os.SystemClock;

import com.example.team_project_team6.ui.walk.WalkViewModel;

import java.util.Locale;

public class StopWatch {
    private long millisecondTime, startTime;
    private int seconds, minutes, hours ;
    private final int TIME_CIRCLE = 60;
    private final int MILLI_TO_SEC = 1000;
    private Handler handler;
    private WalkViewModel walkViewModel;

    public StopWatch(WalkViewModel walkViewModel){
        millisecondTime = 0L ;
        startTime = 0L ;
        seconds = 0 ;
        minutes = 0 ;
        this.walkViewModel = walkViewModel;
        handler = new Handler();
    }


    public void runStopWatch (){
        resetWatch();
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }
    public void stopWatch(){
        handler.removeCallbacks(runnable);
    }

    private void resetWatch(){
        millisecondTime = 0L ;
        startTime = 0L ;
        seconds = 0 ;
        minutes = 0 ;
    }
    private Runnable runnable = new Runnable() {

        public void run() {

            millisecondTime = SystemClock.uptimeMillis() - startTime;

            seconds = (int) (millisecondTime / MILLI_TO_SEC);

            minutes = seconds / TIME_CIRCLE;

            hours = minutes / TIME_CIRCLE;

            minutes = minutes % TIME_CIRCLE;

            seconds = seconds % TIME_CIRCLE;

            walkViewModel.updateStopWatch(String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds));

            handler.postDelayed(this, 0);

        }

    };

}