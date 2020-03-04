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
        data.add(new TeamMember("perry.platypus@gmail.com", "Perry", "Le Platypus"));
        data.add(new TeamMember("sarap.soapbar@gmail.com", "Sarah", "the Soap Bar"));
        data.add(new TeamMember("ellen.elephant@gmail.com", "Ellen", "Elephant"));
        Collections.sort(data);

        mTeamMembers = new MutableLiveData<>(data);
    }

    public void updateMRoutes(ArrayList<TeamMember> routes) {
        mTeamMembers.postValue(routes);
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

    TeamMember getTeamMemberAt(int index) {
        ArrayList<TeamMember> data = mTeamMembers.getValue();

        if (data != null) {
            if (index < data.size() && index >= 0) {
                return data.get(index);
            }
        }

        return null;
    }

    void updateRouteAt(int index, TeamMember newTeamMember) {
        ArrayList<TeamMember> data = mTeamMembers.getValue();

        if (data != null) {
            if (index < data.size() && index >= 0) {
                data.set(index, newTeamMember);
                saveData.saveTeamMember(newTeamMember); // TODO: vestigial structure to mimic saveData.saveRoute(Route)
                mTeamMembers.postValue(data);
            }
        }
    }

    public List<String> getTeamMemberNameList() {
        List<String> teamMemberNames = new ArrayList<>();
        for(TeamMember teamMember : mTeamMembers.getValue()) {
            teamMemberNames.add(teamMember.getFirstName() + " " + teamMember.getLastName());
        }
        return teamMemberNames;
    }

    // Routes are displayed in the same order they are present in
    public LiveData<ArrayList<TeamMember>> getTeamMemberData() {
        // populate list of routes tiles

        Set<String> routeNameSet = saveData.getTeam();
        List<String> routeNameList = new ArrayList<>(routeNameSet);
        Collections.sort(routeNameList);

        ArrayList<Route> routeList = new ArrayList<>();
        for(String routeName : routeNameList) {
            Route route = saveData.getRoute(routeName);
            routeList.add(route);
        }
        mRoutes.postValue(routeList);
        setRouteData(mRoutes);
        return mRoutes;
    }
}
