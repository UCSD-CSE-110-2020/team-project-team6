package com.example.team_project_team6.model;

public class TeamInvite {
    private String teamUUID;
    private String inviter;

    public TeamInvite() {

    }

    public TeamInvite(String teamUUID, String inviter) {
        this.teamUUID = teamUUID;
        this.inviter = inviter;
    }

    public String getTeamUUID() {
        return teamUUID;
    }

    public void setTeamUUID(String teamUUID) {
        this.teamUUID = teamUUID;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }
}
