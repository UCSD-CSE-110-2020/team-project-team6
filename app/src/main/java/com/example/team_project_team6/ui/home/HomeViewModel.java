package com.example.team_project_team6.ui.home;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Integer> mDailySteps;
    private Random rng;

    public HomeViewModel() {
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(1000); // update once a second

        mDailySteps = new MutableLiveData<>();
    }

    // to update this use an async thread and call mDailySteps.postValue
    LiveData<Integer> getDailySteps() {
        return mDailySteps;
    }

    private class AsyncTaskRunner extends AsyncTask<Integer, Integer, Integer> {
        private Random rng;

        @Override
        protected void onPreExecute() {
            rng = new Random(5);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                while (true) {
                    Thread.sleep(params[0]);

                    mDailySteps.postValue(rng.nextInt(100));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}