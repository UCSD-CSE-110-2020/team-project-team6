package com.example.team_project_team6.ui.team;

import android.util.Log;

import com.example.team_project_team6.firebase.IFirebase;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.model.ProposedWalk;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.TeamMember;
import com.example.team_project_team6.model.TeamMessage;
import com.example.team_project_team6.notification.INotification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class TeamViewModel extends ViewModel {

    private MutableLiveData<ArrayList<TeamMember>> mTeamMembers;
    private ArrayList<String> teamInviterNames;
    private MutableLiveData<TeamMember> mInviter;
    private MutableLiveData<Map<String, String>> allMemberGoingStatuses;
    private boolean hasPendingTeamInvite; // records if another user has sent this user a team invite
    private boolean hasProposedWalk; // records if user has proposedWalk
    private boolean hasScheduledWalk;
    private SaveData saveData;

    private boolean inviteIsAccepted; // records whether or not user has accepted or decline the invite
    private boolean isMyProposedWalk; //record if i proposed walk
    private TeamMessage tMessage;

    public TeamViewModel() {
        ArrayList<TeamMember> data = new ArrayList<TeamMember>();
        Map<String,String> goingStatus = new HashMap<>();

//        Mock data
//        goingStatus.put("maryam","accepted");
//        goingStatus.put("cora","declined walk");
//        goingStatus.put("warren","declined time");

        teamInviterNames = new ArrayList<>();
        mTeamMembers = new MutableLiveData<>(data);

        allMemberGoingStatuses = new MutableLiveData<>(goingStatus);
        hasPendingTeamInvite = false;
        hasProposedWalk = false;
    }


    public boolean isMyProposedWalk() {
        return isMyProposedWalk;
    }

    public void setIsMyProposedWalk(boolean myProposedWalk) {
        this.isMyProposedWalk = myProposedWalk;
    }

    public boolean getHasProposedWalk() {
        return hasProposedWalk;
    }

    public void setHasProposedWalk(boolean hasProposedWalk) {
        this.hasProposedWalk = hasProposedWalk;
    }

    public void updateMTeamMembers(ArrayList<TeamMember> teamMembers) {
        mTeamMembers.postValue(teamMembers);
    }

    public void sendProposedWalk(ProposedWalk proposedWalk) {
            saveData.addProposedWalk(proposedWalk);
            //send notification to team
            String message = saveData.getName() + " has proposed the walk!";
            tMessage = new TeamMessage(saveData.getEmail(), message);
            sendTeamNotification(tMessage, true);
    }

    //get the name of user
    public String getName(){
        return saveData.getName();
    }
    //get the uer email
    public String getEmail(){
        return saveData.getEmail();
    }

    public LiveData<ProposedWalk> getProposedWalkData() {
        if(saveData != null) {
            return saveData.getProposedWalk();
        } else {
            return new MutableLiveData<>();
        }
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

    public void setInviteIsAccepted(boolean inviteIsAccepted) {
        this.inviteIsAccepted = inviteIsAccepted;

        if (inviteIsAccepted) {
            saveData.acceptTeamRequest();

            //send notification to team
            String message = saveData.getName() + " has joined the team!";
            tMessage = new TeamMessage(saveData.getEmail(), message);
            sendTeamNotification(tMessage, false);
        } else {
            saveData.declineTeamRequest();
        }
    }

    public void updateMemberGoingData(Map<String, String> memberGoingStatusMap) {
        allMemberGoingStatuses.postValue(memberGoingStatusMap);
    }

    public LiveData<HashMap<String, String>> getAllMemberGoingData() {
        if (saveData != null) {
            return saveData.getMemberGoingStatuses();
        } else {
            return new MutableLiveData<>();
        }
    }

    public ArrayList<String> getAllMemberGoingStatusesExceptSelf() {
        Log.e(TAG,"Team size: " + allMemberGoingStatuses.getValue().size());
        ArrayList<String> memberStatusList =new ArrayList<>();
        for( String member : allMemberGoingStatuses.getValue().keySet()){
            if(!member.equals("self")) {
                String nameAndStatus = member + " " + allMemberGoingStatuses.getValue().get(member);
                memberStatusList.add(nameAndStatus);
            }
        }
        return memberStatusList;
    }

    public void updateMemberGoingStatus(String attendance) {
        saveData.updateMemberGoingStatus(attendance);
        //send notification when member accept or decline proposed walk
        String message = saveData.getName() + " has "+ attendance +" for proposed walk!";
        this.tMessage = new TeamMessage(saveData.getEmail(), message);
        sendTeamNotification(tMessage, true);
    }

    public void sendTeamNotification(TeamMessage message, boolean isMyProposedWalk){
        saveData.sendTeamNotification(message, isMyProposedWalk);
    }
}
