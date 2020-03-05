package com.example.team_project_team6.ui.routes;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.TeamMember;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RoutesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Route>> mRoutes;
    private SaveData saveData;
    private boolean teamView;

    public RoutesViewModel() {
        ArrayList<Route> data = new ArrayList<Route>();
        mRoutes = new MutableLiveData<>(data);
    }

    public void updateMRoutes(ArrayList<Route> routes) {
        mRoutes.postValue(routes);
    }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }

    // Routes are displayed in the same order they are present in
    public LiveData<ArrayList<Route>> getRouteData() {
        if (saveData != null) {
            if (!teamView) {
                return saveData.getAllRoutes();
            } else {
                MutableLiveData<ArrayList<Route>> routes = new MutableLiveData<>(new ArrayList<>());

                saveData.getAllMembers().observeForever(teamMembers -> {
                    for (TeamMember member : teamMembers) {
                        // Skip if its yourself
                        if (member.getEmail().equals(saveData.getEmail())) {
                            continue;
                        }

                        Log.d(TAG, "Adding routes for member " + member.getEmail());

                        String first = member.getFirstName().substring(0, 1);
                        String last = member.getLastName().substring(0, 1);
                        String initials = first + last;

                        saveData.getRoutesFor(member.getEmail()).observeForever(memberRoutes -> {
                            ArrayList<Route> existing_routes = routes.getValue();

                            for (Route r : memberRoutes) {
                                Log.d(TAG, "Adding route " + r.getName() + " from " + member.getEmail());

                                r.setInitials(initials);
                                r.setOwner(member);
                                existing_routes.add(r);
                            }

                            existing_routes.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
                            routes.postValue(existing_routes);
                        });
                    }
                });

                return routes;
            }
        } else {
            return new MutableLiveData<>();
        }
    }

    Route getRouteAt(int index) {
        ArrayList<Route> data = mRoutes.getValue();

        if (data != null) {
            if (index < data.size() && index >= 0) {
                return data.get(index);
            }
        }

        return null;
    }

    void updateRouteAt(int index, Route newRoute) {
        ArrayList<Route> data = mRoutes.getValue();

        if (data != null) {
            if (index < data.size() && index >= 0) {
                data.set(index, newRoute);
                saveData.saveRoute(newRoute);
                mRoutes.postValue(data);
            }
        }
    }

    public void setTeamView(boolean b) {
        teamView = b;
    }
}
