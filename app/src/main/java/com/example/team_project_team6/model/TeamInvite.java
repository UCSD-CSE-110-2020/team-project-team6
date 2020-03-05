package com.example.team_project_team6.model;

public class TeamInvite {
    private String teamUUID;
    private String toOrFrom;
    private String name;
    private String email;
    private String message;

    public TeamInvite() {
        teamUUID = "";
        toOrFrom = "";
        name = "";
        email = "";
        message = "";
    }

    public String getTeamUUID() {
        return teamUUID;
    }

    public void setTeamUUID(String teamUUID) {
        this.teamUUID = teamUUID;
    }

    public String getToOrFrom() {
        return toOrFrom;
    }

    public void setToOrFrom(String toOrFrom) {
        this.toOrFrom = toOrFrom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
