package com.example.team_project_team6.ui.walk;

import android.os.Handler;
import android.os.SystemClock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class WalkViewModel extends ViewModel {

    private long millisecondTime, startTime, timeBuff, updateTime;
    private int seconds, minutes, milliSeconds ;

    private Handler handler;

    private MutableLiveData<String> stopWatch;
    private MutableLiveData<Integer> mWalkSteps;

    public WalkViewModel() {

        millisecondTime = 0L ;
        startTime = 0L ;
        timeBuff = 0L ;
        updateTime = 0L ;
        seconds = 0 ;
        minutes = 0 ;
        milliSeconds = 0 ;
        handler = new Handler();
        startTime = SystemClock.uptimeMillis();
        stopWatch = new MutableLiveData<>();
        stopWatch.setValue("00:00:00");
    }

    public void runStopWatch (){
        handler.postDelayed(runnable, 0);
    }

    public void stopWatch(){
        handler.removeCallbacks(runnable);
    }

    public void resetWatch(){
        millisecondTime = 0L ;
        startTime = 0L ;
        timeBuff = 0L ;
        updateTime = 0L ;
        seconds = 0 ;
        minutes = 0 ;
        milliSeconds = 0 ;
        stopWatch.setValue("00:00:00");
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            millisecondTime = SystemClock.uptimeMillis() - startTime;

            updateTime = timeBuff + millisecondTime;

            seconds = (int) (updateTime / 1000);

            minutes = seconds / 60;

            seconds = seconds % 60;

            milliSeconds = (int) (updateTime % 100);

            stopWatch.setValue(String.format(Locale.ENGLISH, "%02d:%02d:%02d", minutes, seconds, milliSeconds));

            handler.postDelayed(this, 0);

        }

    };

    public LiveData<String> getStopWatch(){
        return stopWatch;
    }

    public LiveData<Integer> getWalkSteps() {
        return mWalkSteps;
    }

    public void updateWalkSteps(int stepCount) {
        mWalkSteps.postValue(stepCount);
    }
}