package com.example.team_project_team6;

import android.app.Application;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.ui.routes.RoutesViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Comparator;

@RunWith(AndroidJUnit4.class)
public class RoutesViewModelTest {
//    @Test
//    public void GetRoutesAlphabeticalOrderTest () {
//        Application app = ApplicationProvider.getApplicationContext();
//        RoutesViewModel viewModel = new RoutesViewModel(app);
//
//        ArrayList<Route> data = viewModel.getRouteData().getValue();
//        ArrayList<Route> sorted_data = data;
//        sorted_data.sort(new Comparator<Route>() {
//            @Override
//            public int compare(Route o1, Route o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        });
//
//        for (int i = 0; i < data.size(); i++) {
//            assert(data.get(i) == sorted_data.get(i));
//        }
//    }
}
