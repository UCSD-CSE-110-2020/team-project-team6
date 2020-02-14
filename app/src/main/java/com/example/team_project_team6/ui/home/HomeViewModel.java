package com.example.team_project_team6.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class HomeViewModel extends ViewModel {

    private MutableLiveData<Long> mDailySteps;

    public HomeViewModel() {
        mDailySteps = new MutableLiveData<>();
    }

    /**
     * returns daily steps stream to subscribe to
     * @return livedata object for daily steps
     */
    LiveData<Long> getDailySteps() {
        return mDailySteps;
    }

    /**
     * updates the daily steps to the current total step count for the day
     * @param stepCount the latest step count for the user
     */
    public void updateDailySteps(long stepCount) {
        mDailySteps.postValue(stepCount);
    }
}