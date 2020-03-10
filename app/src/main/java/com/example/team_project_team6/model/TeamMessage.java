package com.example.team_project_team6.model;

public class TeamMessage {
    private String fromEmail;
    private String message;

    public TeamMessage(String fromEmail, String message) {
        this.fromEmail = fromEmail;
        this.message = message;
    }
    public void setFromEmail() {
        this.fromEmail = fromEmail;
    }

    public void setMessage() {
        this.message = message;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return fromEmail +
                ":\n" +
                message +
                "\n" +
                "---\n";
    }
}
