package com.example.team_project_team6.ui.walk;

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
import androidx.lifecycle.ViewModelProviders;

import com.example.team_project_team6.R;

public class WalkFragment extends Fragment {

    private WalkViewModel walkViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        walkViewModel =
                ViewModelProviders.of(this).get(WalkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_walk, container, false);

        final Button start_end_button = root.findViewById(R.id.toggle_walk);
        if (walkViewModel.is_currently_walking().getValue()) {
            start_end_button.setText(R.string.end_walk);
        } else {
            start_end_button.setText(R.string.start_walk);
        }

        start_end_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walkViewModel.is_currently_walking().getValue()) {
                    walkViewModel.end_walking();
                    start_end_button.setText(R.string.start_walk);
                } else {
                    walkViewModel.start_walking();
                    start_end_button.setText(R.string.end_walk);
                }
            }
        });

        final TextView text_current_route = root.findViewById(R.id.current_walk_name);
        walkViewModel.getRouteName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                text_current_route.setText(s);
            }
        });

        final TextView text_current_steps = root.findViewById(R.id.textCurrentSteps);
        final TextView text_current_dist = root.findViewById(R.id.textCurrentDist);
        walkViewModel.getCurrentSteps().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer num) {
                final int height_in_inches = 66;
                final double stride_dist_in_ft = (0.413 * (double)height_in_inches) / 12.0;
                double dist = stride_dist_in_ft * num / 5280.0;

                text_current_steps.setText(String.format("%d steps", num));
                text_current_dist.setText(String.format("%.2f", dist) + " mi");
            }
        });

        final TextView text_current_time = root.findViewById(R.id.text_timer);
        walkViewModel.getCurrentTimeInSeconds().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer seconds) {
                Integer minutes = (seconds / 60) % 60;
                Integer hours = seconds / 3600;
                String time = String.format("%02d:%02d", hours, minutes);

                text_current_time.setText(time);
            }
        });

        return root;
    }
}