package com.example.team_project_team6.ui.team;

import android.app.Application;
import android.content.Context;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TeamViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<String>> mTeamMemberNames;

    public TeamViewModel() {
        ArrayList<String> data = new ArrayList<String>();

        // Mock names
        data.add("Perry the Platypus");
        data.add("Sarah the Soap bar");
        data.add("Ellen the Elephant");
        Collections.sort(data);

        mTeamMemberNames = new MutableLiveData<>(data);
    }

    public ArrayList<String> getTeamMemberNameList() {
        return mTeamMemberNames.getValue();
    }
}
