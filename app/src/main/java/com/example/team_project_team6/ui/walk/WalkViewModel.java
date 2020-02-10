package com.example.team_project_team6.ui.walk;

import android.os.Handler;
import android.os.SystemClock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class WalkViewModel extends ViewModel {

    private MutableLiveData<String> stopWatch;
    private MutableLiveData<Long> mWalkSteps;
    private MutableLiveData<Boolean> mCurrentlyWalking;

    public WalkViewModel() {
        mCurrentlyWalking = new MutableLiveData<>(false);
        mWalkSteps = new MutableLiveData<>();
        stopWatch = new MutableLiveData<>();
        stopWatch.setValue("00:00:00");
    }

    void start_walking() {
        mCurrentlyWalking.postValue(true);
    }

    void end_walking() {
        mCurrentlyWalking.postValue(false);
    }

    LiveData<Boolean> is_currently_walking() {
        return mCurrentlyWalking;
    }

    public void updateStopWatch(String s){
        stopWatch.postValue(s);
    }

    public LiveData<String> getStopWatch(){
        return stopWatch;
    }

    public LiveData<Long> getWalkSteps() {
        return mWalkSteps;
    }

    public void updateWalkSteps(long stepCount) {
        mWalkSteps.postValue(stepCount);
    }

}