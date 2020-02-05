package com.example.team_project_team6.ui.walk;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WalkViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WalkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is walk fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}