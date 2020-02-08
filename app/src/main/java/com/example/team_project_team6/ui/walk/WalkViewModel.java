package com.example.team_project_team6.ui.walk;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.R;

import java.util.Calendar;

public class WalkViewModel extends ViewModel {

    private MutableLiveData<String> mRouteName;
    private MutableLiveData<Integer> mCurrentSteps;
    private MutableLiveData<Boolean> mCurrentlyWalking;
    private MutableLiveData<Integer> mTimeInSeconds;

    public WalkViewModel() {
        mRouteName = new MutableLiveData<>("New Walk");
        mCurrentSteps = new MutableLiveData<>(0);
        mCurrentlyWalking = new MutableLiveData<>(false);
        mTimeInSeconds = new MutableLiveData<>(0);
    }

    // called whenever the user hits the walk/stop button
    void start_walking() {
        mCurrentlyWalking.postValue(true);
    };

    void end_walking() {
        mCurrentlyWalking.postValue(false);
    };

    LiveData<Boolean> is_currently_walking() {
        return mCurrentlyWalking;
    }

    LiveData<String> getRouteName() {
        return mRouteName;
    }

    LiveData<Integer> getCurrentSteps() { return mCurrentSteps; }

    LiveData<Integer> getCurrentTimeInSeconds() { return mTimeInSeconds; }
}