package com.example.team_project_team6.model;

import java.util.HashMap;

public class TeamMember {
    private String email;
    private String firstName;
    private String lastName;

    public TeamMember() {

    }

    public TeamMember(HashMap<String, String> map) {
        this.email = map.get("email");
        this.firstName = map.get("firstName");
        this.lastName = map.get("lastName");
    }

    public TeamMember(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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

}
