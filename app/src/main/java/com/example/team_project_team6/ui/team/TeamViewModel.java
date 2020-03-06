package com.example.team_project_team6.ui.team;

import android.util.Log;

import com.example.team_project_team6.firebase.IFirebase;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.TeamMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TeamViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TeamMember>> mTeamMembers;
    private ArrayList<String> teamInviterNames;
    private MutableLiveData<TeamMember> mInviter;
    private SaveData saveData;
    private boolean hasPendingTeamInvite; // records if another user has sent this user a team invite
    private boolean inviteIsAccepted; // records whether or not user has accepted or decline the invite
    private IFirebase adapter;
    public TeamViewModel() {
        ArrayList<TeamMember> data = new ArrayList<TeamMember>();
        mTeamMembers = new MutableLiveData<>(data);

        teamInviterNames = new ArrayList<>();
    }

    public void setFbaseAdapter(IFirebase adapter){
        this.adapter = adapter;
    }

    public IFirebase getFbaseAdaper(){
        return adapter;
    }

    public void updateMTeamMembers(ArrayList<TeamMember> teamMembers) {
        mTeamMembers.postValue(teamMembers);
    }

    public void addTeamInviterName(String inviterName) {
        this.teamInviterNames.add(inviterName);
        Collections.sort(teamInviterNames);
    }

    public void removeTeamInviterName(String inviterName) {
        this.teamInviterNames.remove(inviterName);
    }

    public ArrayList<String> getTeamInviterNames() {
        return this.teamInviterNames;
    }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }

    public SaveData getSaveData(SaveData saveData) {
        return this.saveData;
    }

    public ArrayList<String> getTeamMemberNames(ArrayList<TeamMember> members) {
        ArrayList<String> names = new ArrayList<>();

        assert members != null;
        for (TeamMember m : members) {
            if (m.getEmail().equals(saveData.getEmail())) continue;
            names.add(m.getFirstName() + " " + m.getLastName());
        }

        return names;
    }

    public void sendTeamRequest(String email) {
        saveData.addTeamMember(email);
    }

    public LiveData<ArrayList<TeamMember>> getTeamMemberData() {
        if (saveData != null) {
            return saveData.getAllMembers();
        } else {
            return new MutableLiveData<>();
        }
    }


    public LiveData<HashMap<String, String>> getTeamInviterData() {
        if(saveData != null) {
            Log.i("getTeamInviterData in ViewModel",  "inviter: " + saveData.getTeamInviter().toString());
            return saveData.getTeamInviter();
        } else {
            return new MutableLiveData<>();
        }
    }

    public void setHasPendingTeamInvite(boolean hasPendingTeamInvite) {
        this.hasPendingTeamInvite = hasPendingTeamInvite;
    }

    public boolean getHasPendingTeamInvite() {
        return hasPendingTeamInvite;
    }

    public boolean getInviteIsAccepted() {
        return inviteIsAccepted;
    }

    public void setInviteIsAccepted(boolean inviteIsAccepted) {
        this.inviteIsAccepted = inviteIsAccepted;

        if (inviteIsAccepted) {
            saveData.acceptTeamRequest();
        } else {
            saveData.declineTeamRequest();
        }
    }
}
