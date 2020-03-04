package com.example.team_project_team6.ui.team;

import android.app.Application;
import android.content.Context;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.TeamMember;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TeamViewModel extends ViewModel {
    private MutableLiveData<ArrayList<String>> mTeamMemberNames;
    private SaveData saveData;
    private MutableLiveData<ArrayList<TeamMember>> mTeamMembers;

    public TeamViewModel() {
        ArrayList<String> data = new ArrayList<String>();

        // Mock names
        data.add("Perry the Platypus");
        data.add("Sarah the Soap bar");
        data.add("Ellen the Elephant");
        Collections.sort(data);

        mTeamMemberNames = new MutableLiveData<>(data);
        mTeamMembers = new MutableLiveData<>();
    }

    public SaveData getSaveData() {
        return saveData;
    }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }

    public ArrayList<String> getTeamMemberNameList() {
        return mTeamMemberNames.getValue();
    }

    public ArrayList<String> getTeamMembers() {
        ArrayList<TeamMember> members = getTeamData().getValue();
        ArrayList<String> names = new ArrayList<>();

        assert members != null;
        for (TeamMember m : members) {
            if (m.getEmail().equals(saveData.getEmail())) continue;
            names.add(m.getFirstName() + " " + m.getLastName());
        }

        return names;
    }

    public LiveData<ArrayList<TeamMember>> getTeamData() {
        return saveData.getAllMembers();
    }
}
