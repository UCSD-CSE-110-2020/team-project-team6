package com.example.team_project_team6.ui.routes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;

import java.util.ArrayList;

public class RoutesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Route>> mRoutes;
    private SaveData saveData;

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

    public SaveData getSaveData(SaveData saveData) {
        return this.saveData;
    }

    // mRoutes stores a list of all the routes in firebase
    public void setRouteData(MutableLiveData<ArrayList<Route>> mRoutes) {
        this.mRoutes = mRoutes;
    }

    // Routes are displayed in the same order they are present in
    public LiveData<ArrayList<Route>> getRouteData() {
        if (saveData != null) {
            return saveData.getAllRoutes();
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
}