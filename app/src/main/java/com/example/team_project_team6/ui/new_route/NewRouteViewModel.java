package com.example.team_project_team6.ui.new_route;

import com.example.team_project_team6.firebase.IFirebase;

import androidx.lifecycle.ViewModel;

public class NewRouteViewModel extends ViewModel {
    private IFirebase adapter;

    public void setAdapter(IFirebase adapter) {
        this.adapter = adapter;
    }

    public IFirebase getAdapter() {
        return adapter;
    }
}
