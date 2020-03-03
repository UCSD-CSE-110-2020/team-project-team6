package com.example.team_project_team6.ui.home;

import com.example.team_project_team6.model.SaveData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class HomeViewModel extends ViewModel {

    private MutableLiveData<Long> mDailySteps;

    public SaveData getSaveData() {
        return saveData;
    }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }

    private SaveData saveData;

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

    /**
     * returns daily step count
     * @return the daily step count
     */
    public Long getDailyStepCount() { return mDailySteps.getValue(); }

    public int getHeight() {
        if (saveData == null) {
            return 0;
        }

        // get the height from SharedPreferences and calculate stride distance
        return saveData.getHeight();
    }
}