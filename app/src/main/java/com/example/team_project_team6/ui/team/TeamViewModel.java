package com.example.team_project_team6.ui.team;

import android.app.Application;
import android.content.Context;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.TeamMember;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TeamViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TeamMember>> mTeamMembers;
    private SaveData saveData;

    public TeamViewModel() {
        ArrayList<TeamMember> data = new ArrayList<TeamMember>();
        mTeamMembers = new MutableLiveData<>(data);

        // Mock names
/*        data.add(new TeamMember("perry.platypus@gmail.com", "Perry", "Le Platypus"));
        data.add(new TeamMember("sarap.soapbar@gmail.com", "Sarah", "the Soap Bar"));
        data.add(new TeamMember("ellen.elephant@gmail.com", "Ellen", "Elephant"));
*/
        mTeamMembers = new MutableLiveData<>(data);
    }

    public void updateMTeamMembers(ArrayList<TeamMember> teamMembers) {
        mTeamMembers.postValue(teamMembers);
    }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }

    public SaveData getSaveData(SaveData saveData) {
        return this.saveData;
    }

    // mTeamMembers stores a list of all the routes in firebase
    public void setTeamMemberData(MutableLiveData<ArrayList<TeamMember>> mTeamMembers) {
        this.mTeamMembers = mTeamMembers;
    }

    public TeamMember getTeamMemberAt(int index) {
        ArrayList<TeamMember> data = mTeamMembers.getValue();

        if (data != null) {
            if (index < data.size() && index >= 0) {
                return data.get(index);
            }
        }

        return null;
    }

    public void updateTeamMemberAt(int index, TeamMember newTeamMember) {
        ArrayList<TeamMember> data = mTeamMembers.getValue();

        if (data != null) {
            if (index < data.size() && index >= 0) {
                data.set(index, newTeamMember);
                saveData.saveTeamMember(newTeamMember);
                mTeamMembers.postValue(data);
            }
        }
    }

    public LiveData<ArrayList<TeamMember>> getTeamMemberData() {
        if (saveData != null) {
            return saveData.getAllMembers();
        } else {
            return new MutableLiveData<>();
        }
    }

    public LiveData<TeamMember> getTeamInviterData() {
        return null;
    }
}
