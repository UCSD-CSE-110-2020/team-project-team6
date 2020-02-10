package com.example.team_project_team6.ui.home;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.fitness.FitnessService;
import com.example.team_project_team6.fitness.FitnessServiceFactory;

import java.util.Random;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Integer> mDailySteps;

    public HomeViewModel() {

//        AsyncTaskRunner runner = new AsyncTaskRunner();
//        runner.execute(1000); // update once a second

        mDailySteps = new MutableLiveData<>();
    }

    // to update this use an async thread and call mDailySteps.postValue
    LiveData<Integer> getDailySteps() {
        return mDailySteps;
    }

    public void updateDailySteps(int stepCount) {
        mDailySteps.postValue(stepCount);
    }

//    private class AsyncTaskRunner extends AsyncTask<Integer, Integer, Integer> {
//
//        @Override
//        protected void onPreExecute() {}
//
//        @Override
//        protected Integer doInBackground(Integer... params) {
//            try {
//                while (true) {
//                    Thread.sleep(params[0]);
//                    mDailySteps.postValue(1);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//    }
}