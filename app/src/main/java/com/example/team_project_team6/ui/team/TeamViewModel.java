package com.example.team_project_team6.ui.team;

import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.TeamMember;

import java.util.ArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TeamViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TeamMember>> mTeamMembers;
    private SaveData saveData;
    private boolean hasPendingTeamInvite; // records if another user has sent this user a team invite

    public TeamViewModel() {
        ArrayList<TeamMember> data = new ArrayList<TeamMember>();
        mTeamMembers = new MutableLiveData<>(data);

        // Mock names
/*        data.add(new TeamMember("perry.platypus@gmail.com", "Perry", "Le Platypus"));
        data.add(new TeamMember("sarap.soapbar@gmail.com", "Sarah", "the Soap Bar"));
        data.add(new TeamMember("ellen.elephant@gmail.com", "Ellen", "Elephant"));
*/
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

    public ArrayList<String> getTeamMemberNames(ArrayList<TeamMember> members) {
        ArrayList<String> names = new ArrayList<>();

        assert members != null;
        for (TeamMember m : members) {
            if (m.getEmail().equals(saveData.getEmail())) continue;
            names.add(m.getFirstName() + " " + m.getLastName());
        }

        return names;
    }

    public LiveData<ArrayList<TeamMember>> getTeamMemberData() {
        if (saveData != null) {
            return saveData.getAllMembers();
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

    public LiveData<TeamMember> getTeamInviterData() {
        return null;
    }
}
