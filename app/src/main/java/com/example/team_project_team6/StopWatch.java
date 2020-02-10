package com.example.team_project_team6;

import android.os.Handler;
import android.os.SystemClock;

import com.example.team_project_team6.ui.walk.WalkViewModel;

import java.util.Locale;

public class StopWatch {
    private long millisecondTime, startTime;
    private int seconds, minutes, hours ;
    private Handler handler;

    private WalkViewModel walkViewModel;

    public StopWatch(){
        millisecondTime = 0L ;
        startTime = 0L ;
        seconds = 0 ;
        minutes = 0 ;
        handler = new Handler();
    }
    public void runStopWatch (WalkViewModel walkViewModel){
        resetWatch();
        this.walkViewModel = walkViewModel;
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }
    public void stopWatch(){
        handler.removeCallbacks(runnable);
    }
    public void resetWatch(){
        millisecondTime = 0L ;
        startTime = 0L ;
        seconds = 0 ;
        minutes = 0 ;
    }
    private Runnable runnable = new Runnable() {

        public void run() {

            millisecondTime = SystemClock.uptimeMillis() - startTime;


            seconds = (int) (millisecondTime / 1000);

            minutes = seconds / 60;

            hours = minutes / 60;

            seconds = seconds % 60;

            walkViewModel.updateStopWatch(String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds));
            //stopWatch.setValue(String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds));

            handler.postDelayed(this, 0);

        }

    };
}
