package com.example.team_project_team6.model;

import android.graphics.Color;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class TeamMember {
    private String email;
    private String firstName;
    private String lastName;

    public TeamMember() {
        email = "";
        firstName = "";
        lastName = "";
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

    public int getColor() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            final byte[] digest = md.digest(this.email.getBytes());
            ByteBuffer wrapper = ByteBuffer.wrap(digest);
            Color color = Color.valueOf(wrapper.getInt());

            return Color.rgb(color.red(), color.green(), color.blue());
        } catch (NoSuchAlgorithmException e) {
            // Should never fail
            e.printStackTrace();
        }

        return 0;
    }
}
