package com.example.team_project_team6.ui.team;

import android.util.Log;

import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.TeamMember;

import java.util.ArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TeamViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TeamMember>> mTeamMembers;
    private MutableLiveData<TeamMember> mInviter;
    private SaveData saveData;
    private boolean hasPendingTeamInvite; // records if another user has sent this user a team invite

    private boolean inviteIsAccepted;

    public TeamViewModel() {
        ArrayList<TeamMember> data = new ArrayList<TeamMember>();
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

    public LiveData<String> getTeamInviterData() {
        Log.e("TeamViewModel getTeamInviterData", "saveData is null: " + (saveData == null));
        if(saveData != null) {
//            Log.e("getTeamInviterData in ViewModel",  "inviter: " + saveData.getTeamInviter().toString());
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
    }
}
