package com.example.team_project_team6.ui.team;

import android.app.Application;
import android.content.Context;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TeamViewModel extends ViewModel {
    private MutableLiveData<ArrayList<String>> mTeamMemberNames;
    private boolean hasPendingTeamInvite; // records if another user has sent this user a team invite

    public TeamViewModel() {
        ArrayList<String> data = new ArrayList<String>();

        // Mock names
        data.add("Perry the Platypus");
        data.add("Sarah the Soap bar");
        data.add("Ellen the Elephant");
        Collections.sort(data);

        mTeamMemberNames = new MutableLiveData<>(data);

        hasPendingTeamInvite = false;
    }

    public void setHasPendingTeamInvite(boolean hasPendingTeamInvite) {
        this.hasPendingTeamInvite = hasPendingTeamInvite;
    }

    public boolean getHasPendingTeamInvite() {
        return hasPendingTeamInvite;
    }

    public ArrayList<String> getTeamMemberNameList() {
        return mTeamMemberNames.getValue();
    }
}
