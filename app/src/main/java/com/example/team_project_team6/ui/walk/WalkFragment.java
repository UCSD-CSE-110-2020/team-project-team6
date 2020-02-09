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

    private WalkViewModel dashboardViewModel;
    private boolean switchButton = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        dashboardViewModel =
                ViewModelProviders.of(this).get(WalkViewModel.class);
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


        dashboardViewModel.getStopWatch().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                lbStopWatch.setText(s);
            }
        });

        return root;
    }
}