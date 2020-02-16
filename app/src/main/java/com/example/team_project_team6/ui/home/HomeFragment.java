package com.example.team_project_team6.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;

import java.util.Locale;

public class HomeFragment extends Fragment {

    @VisibleForTesting
    HomeViewModel homeViewModel;
    @VisibleForTesting
    double strideDistInFt;
    private TextView dailySteps, dailyDist;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (homeViewModel == null) {
            homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        }
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        int heightInInches = getHeight();
        strideDistInFt = (0.413 * (double) heightInInches) / 12.0;

        Log.d("Home", "Height in inches: " + heightInInches);
        Log.d("Home", "Stride dist in feet: " + strideDistInFt);

        // save references to the steps and distance TextViews
        dailySteps = root.findViewById(R.id.textDailySteps);
        dailyDist = root.findViewById(R.id.textDailyDist);

        // check if previous screen is RouteDetailsFragment to prevent creation of another walk object
        RouteDetailsViewModel detailsViewModel = new ViewModelProvider(requireActivity()).get(RouteDetailsViewModel.class);
        detailsViewModel.setIsWalkFromRouteDetails(false);

        // Bind model data to textviews
        bindViews();

        return root;
    }

    void bindViews() {
        Log.d("Home", "Rebinding views to viewmodel");

        homeViewModel.getDailySteps().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long steps) {
                if (steps == null) {
                    steps = 0L;
                }

                double dist = strideDistInFt * steps / 5280.0;

                dailySteps.setText(String.format(Locale.ENGLISH, "%d steps", steps));
                dailyDist.setText(String.format(Locale.ENGLISH, "%.2f mi", dist));
            }
        });
    }

    int getHeight() {
        // save reference to MainActivity and create object to handle SharedPreferences calls
        final SaveData saveData = new SaveData(requireActivity());

        // get the height from SharedPreferences and calculate stride distance
        return saveData.getHeight();
    }
}