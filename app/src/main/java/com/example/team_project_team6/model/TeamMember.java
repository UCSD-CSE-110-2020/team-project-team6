package com.example.team_project_team6.model;

import java.util.HashMap;

public class TeamMember {
    private String email;
    private String firstName;
    private String lastName;
    private boolean pending;

    public TeamMember() {
        email = "";
        firstName = "";
        lastName = "";
        pending = false;
    }

    public TeamMember(HashMap<String, Object> map) {
        this.email = (String) map.get("email");
        this.firstName = (String) map.get("firstName");
        this.lastName = (String) map.get("lastName");
        this.pending = (Boolean) map.get("pending");
    }

    public TeamMember(String email, String firstName, String lastName, boolean pending) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pending = pending;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
