package com.example.team_project_team6.ui.walk;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.StopWatch;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WalkViewModel extends AndroidViewModel {

    private MutableLiveData<String> stopWatch;
    private MutableLiveData<Long> mWalkSteps;
    private MutableLiveData<Boolean> mCurrentlyWalking;
    private MutableLiveData<Boolean> mCurrentlyWalkingMock;
    private StopWatch sw;
    private boolean isWalking;
    private Context context;

    public WalkViewModel(Application application) {
        super(application);
        mCurrentlyWalking = new MutableLiveData<>(false);
        mCurrentlyWalkingMock = new MutableLiveData<>(false);
        mWalkSteps = new MutableLiveData<>();
        stopWatch = new MutableLiveData<>();
        stopWatch.setValue("00:00:00");
        this.context = application;
        sw = new StopWatch();
    }

    /**
     * informs observers that walk is underway by updating to the stream
     */
    void startWalking(boolean isMockWalk) {
        if(!isMockWalk) {
            mCurrentlyWalking.postValue(true);
        } else {
            mCurrentlyWalkingMock.postValue(true);
        }
    }

    /**
     * informs observers that walk is not underway by updating to the stream
     */
    void endWalking(boolean isMockWalk) {
        if(!isMockWalk) {
            mCurrentlyWalking.postValue(false);
        } else {
            mCurrentlyWalkingMock.postValue(false);
        }

    }

    /**
     * returns walking status stream to subscribe to
     * @return livedata object for walking status
     */
    LiveData<Boolean> isCurrentlyWalking(boolean isMockWalk) {
        if(!isMockWalk) {
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

    public void resetStepsToZero() {
        mWalkSteps.postValue(0l);
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