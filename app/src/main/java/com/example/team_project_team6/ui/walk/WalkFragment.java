package com.example.team_project_team6.ui.walk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.team_project_team6.R;

import java.util.Locale;

public class WalkFragment extends Fragment {

    private WalkViewModel dashboardViewModel;
    private boolean switchButton = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel = new ViewModelProvider(requireActivity()).get(WalkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_walk, container, false);

        final TextView textView = root.findViewById(R.id.text_walk);
        final TextView lbStopWatch = root.findViewById(R.id.lbTime);
        final Button btStart = root.findViewById(R.id.btStart);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!switchButton) {
                    dashboardViewModel.runStopWatch();
                    //switch to stop button when it's true
                    switchButton = true;
                    btStart.setText(R.string.bt_stop);
                }else {
                    switchButton = false;
                    btStart.setText(R.string.bt_start);
                    dashboardViewModel.stopWatch();
                    //dashboardViewModel.resetWatch();
                }
            }
        });

        final TextView walkSteps = root.findViewById(R.id.lbStep);
        final TextView walkDist = root.findViewById(R.id.lbDistance);
        SharedPreferences spfs = this.requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        final int heightInInches = spfs.getInt("user_height", -1);
        final double strideDistInFt = (0.413 * (double) heightInInches) / 12.0;

        dashboardViewModel.getWalkSteps().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long num) {

                if (num == null) {
                    num = 0l;
                }

                double dist = strideDistInFt * num / 5280.0;

                walkSteps.setText(String.format(Locale.ENGLISH, "%d steps", num));
                walkDist.setText(String.format(Locale.ENGLISH, "%.2f mi", dist));
            }
        });

        dashboardViewModel.getStopWatch().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                lbStopWatch.setText(s);
            }
        });

        return root;
    }
}