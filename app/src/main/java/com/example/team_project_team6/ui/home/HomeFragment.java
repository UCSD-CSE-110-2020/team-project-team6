package com.example.team_project_team6.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.team_project_team6.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView dailySteps = root.findViewById(R.id.textDailySteps);
        final TextView dailyDist = root.findViewById(R.id.textDailyDist);
        homeViewModel.getDailySteps().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer num) {
                final int height_in_inches = 66;
                final double stride_dist_in_ft = (0.413 * (double)height_in_inches) / 12.0;
                double dist = stride_dist_in_ft * num / 5280.0;

                dailySteps.setText(num.toString() + " steps");
                dailyDist.setText(String.format("%.2f", dist) + " mi");
            }
        });

        return root;
    }
}