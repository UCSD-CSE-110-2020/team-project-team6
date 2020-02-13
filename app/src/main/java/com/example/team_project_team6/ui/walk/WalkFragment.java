package com.example.team_project_team6.ui.walk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;

import java.util.Locale;

public class WalkFragment extends Fragment {

    private WalkViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel = new ViewModelProvider(requireActivity()).get(WalkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_walk, container, false);

        final TextView textView = root.findViewById(R.id.text_walk);
        final TextView lbStopWatch = root.findViewById(R.id.lbTime);
        final Button btStart = root.findViewById(R.id.btStart);
        final TextView walkSteps = root.findViewById(R.id.lbStep);
        final TextView walkDist = root.findViewById(R.id.lbDistance);

        final MainActivity mainActivity = (MainActivity) getActivity();

        if(dashboardViewModel.is_currently_walking().getValue()) {
            btStart.setText(R.string.bt_stop);
        }else {
            btStart.setText(R.string.bt_start);
        }

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dashboardViewModel.is_currently_walking().getValue()) {
                    System.out.println("is there herrrrrrrrrrrre");
                    mainActivity.runStopWatch();
                    dashboardViewModel.start_walking();
                    btStart.setText(R.string.bt_stop);

                }else {
                    dashboardViewModel.end_walking();
                    btStart.setText(R.string.bt_start);
                    mainActivity.stopWatch();

                    // reset values
                    walkSteps.setText(R.string.walk_step_empty);
                    walkDist.setText(R.string.dist_empty);

                    //show data when the walk has done!
                    Toast.makeText(getActivity(), "time: " + lbStopWatch.getText().toString(), Toast.LENGTH_LONG).show();

                }
            }
        });

        SharedPreferences spfs = this.requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        final int heightInInches = spfs.getInt("user_height", -1);
        final double strideDistInFt = (0.413 * (double) heightInInches) / 12.0;

         dashboardViewModel.getWalkSteps().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long num) {

                if (num == null) {
                    num = 0L;
                }

                double dist = strideDistInFt * num / 5280.0;

                walkSteps.setText(String.format(Locale.ENGLISH, "%d", num));
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