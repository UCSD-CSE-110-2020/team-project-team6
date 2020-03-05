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
    private MutableLiveData<ArrayList<String>> memberGoingStatus;
    private boolean hasPendingTeamInvite; // records if another user has sent this user a team invite
    private boolean hasProposedWalk; // records if user has proposedWalk

    public boolean isMyProposedWalk() {
        return isMyProposedWalk;
    }

    public void setMyProposedWalk(boolean myProposedWalk) {
        isMyProposedWalk = myProposedWalk;
    }

    private boolean isMyProposedWalk;

    public boolean getHasProposedWalk() {
        return hasProposedWalk;
    }

    public void setHasProposedWalk(boolean hasProposedWalk) {
        this.hasProposedWalk = hasProposedWalk;
    }



    public TeamViewModel() {
        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> goingStatus = new ArrayList<String>();

        // Mock names
        data.add("Perry the Platypus");
        data.add("Sarah the Soap bar");
        data.add("Ellen the Elephant");
        Collections.sort(data);
//??????????????
        mTeamMemberNames = new MutableLiveData<>(data);
        memberGoingStatus = new MutableLiveData<>(goingStatus);
        hasPendingTeamInvite = false;
        hasProposedWalk = false;

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
    public ArrayList<String> getMemberGoingStatus() { return memberGoingStatus.getValue();
    }
}
