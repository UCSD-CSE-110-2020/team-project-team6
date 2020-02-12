package com.example.team_project_team6.ui.route_details;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Route;

public class RouteDetailsFragment extends Fragment {

    private RouteDetailsViewModel mViewModel;
    private AppCompatActivity mActivity;
    private Route route;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(requireActivity()).get(RouteDetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_routes, container, false);

        mActivity = (AppCompatActivity) requireActivity();

        route = mViewModel.getRoute();
        set_activity_name(route.getName());

        Log.d("Route Details", route.getName());

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void set_activity_name(String title) {
        ActionBar supportActionBar = mActivity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    private void add_star_icon() {

    }
}
