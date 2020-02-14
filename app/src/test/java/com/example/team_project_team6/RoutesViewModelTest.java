package com.example.team_project_team6;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.ui.routes.RoutesViewModel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;

public class RoutesViewModelTest {
    @Test
    public void GetRoutesAlphabeticalOrderTest () {
        // TODO: Mock data access to check that the returned data is in alphabetical order
        RoutesViewModel viewModel = new RoutesViewModel();

        ArrayList<Route> data = viewModel.getRouteData().getValue();
        ArrayList<Route> sorted_data = data;
        sorted_data.sort(new Comparator<Route>() {
            @Override
            public int compare(Route o1, Route o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (int i = 0; i < data.size(); i++) {
            assert(data.get(i) == sorted_data.get(i));
        }
    }
}
