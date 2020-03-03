package com.example.team_project_team6.ui.walk;

import android.app.Application;
import android.content.Context;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.StopWatch;
import com.example.team_project_team6.model.Walk;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class WalkViewModel extends AndroidViewModel {

    private MutableLiveData<String> stopWatch;
    private MutableLiveData<Long> mWalkSteps;
    private MutableLiveData<Boolean> isMockWalk;
    private MutableLiveData<Boolean> mCurrentlyWalking;
    private MutableLiveData<Boolean> mCurrentlyWalkingMock;
    private StopWatch sw;
    private boolean isWalking;
    private Context context;
    private SaveData saveData;

    public WalkViewModel(Application application) {
        super(application);
        isMockWalk = new MutableLiveData<>(false);
        mCurrentlyWalking = new MutableLiveData<>(false);
        mCurrentlyWalkingMock = new MutableLiveData<>(false);
        mWalkSteps = new MutableLiveData<>();
        stopWatch = new MutableLiveData<>();
        stopWatch.setValue("00:00:00");
        this.context = application;
        sw = new StopWatch();
    }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }

    public int getHeight() {
        return saveData.getHeight();
    }

    /**
     * informs observers that walk is underway by updating to the stream
     */
    public void startWalking() {
        if(!getIsMockWalk()) {
            mCurrentlyWalking.postValue(true);
        } else {
            mCurrentlyWalkingMock.postValue(true);
        }
    }

    public void saveRoute(Route route) {
        saveData.saveRoute(route);
    }

    public void saveWalk(Walk walk) {
        saveData.saveWalk(walk);
    }
    /**
     * informs observers that walk is not underway by updating to the stream
     */
    public void endWalking() {
        if(!getIsMockWalk()) {
            mCurrentlyWalking.postValue(false);
        } else {
            mCurrentlyWalkingMock.postValue(false);
        }

    }

    void setIsMockWalk(boolean isMockWalk) {
        this.isMockWalk.postValue(isMockWalk);
    }

    public boolean getIsMockWalk() {
        return this.isMockWalk.getValue();
    }

    /**
     * returns walking status stream to subscribe to
     * @return livedata object for walking status
     */
    public LiveData<Boolean> isCurrentlyWalking() {
        if(!getIsMockWalk()) {
            return mCurrentlyWalking;
        } else {
            return mCurrentlyWalkingMock;
        }

    }

    /**
     * posts latest stopwatch value
     * @param s the updated stopwatch time
     */
    public void updateStopWatch(String s){
        stopWatch.postValue(s);
    }

    /**
     * returns stopwatch stream to subscribe to
     * @return livedata object for stopwatch time
     */
    public LiveData<String> getStopWatch(){
        return stopWatch;
    }

    /**
     * returns walk steps stream to subscribe to
     * @return livedata object for latest number of steps on walk
     */
    public LiveData<Long> getWalkSteps() {
        return mWalkSteps;
    }

    /**
     * posts latest step count value
     * @param stepCount latest number of steps user has walked
     */
    public void updateWalkSteps(long stepCount) {
        mWalkSteps.postValue(stepCount);
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void resetToZero() {
        mWalkSteps.postValue(0l);
        updateStopWatch("00:00:00");
    }

    /**
     * start the walk stopwatch and set walk mode for step-tracking to on
     */
    public void runStopWatch () {
        sw.runStopWatch(this);
        isWalking = true;
    }

    /**
     * stop the stopwatch and set walk mode for step tracking to off
     */
    public void stopWatch() {
        sw.stopWatch();
        isWalking = false;
    }
}