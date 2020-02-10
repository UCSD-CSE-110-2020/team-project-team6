package com.example.team_project_team6.ui.home;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.fitness.FitnessService;
import com.example.team_project_team6.fitness.FitnessServiceFactory;

import java.util.Random;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Long> mDailySteps;

    public HomeViewModel() {

//        AsyncTaskRunner runner = new AsyncTaskRunner();
//        runner.execute(1000); // update once a second

        mDailySteps = new MutableLiveData<>();
    }

    // to update this use an async thread and call mDailySteps.postValue
    LiveData<Long> getDailySteps() {
        return mDailySteps;
    }

    public void updateDailySteps(long stepCount) {
        mDailySteps.postValue(stepCount);
    }
}