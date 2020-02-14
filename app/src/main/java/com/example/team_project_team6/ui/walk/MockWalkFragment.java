package com.example.team_project_team6.ui.walk;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MockWalk.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MockWalk#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MockWalk extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MockWalk() {
        // Required empty public constructor
    }


    private WalkViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel = new ViewModelProvider(requireActivity()).get(WalkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_walk, container, false);

        final TextView textView = root.findViewById(R.id.text_walk);
        final TextView lbStopWatch = root.findViewById(R.id.lbTime);
        final Button btStart = root.findViewById(R.id.btStart);

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

                    //show data when the walk has done!
                    Toast.makeText(getActivity(), "time: " + lbStopWatch.getText().toString(), Toast.LENGTH_LONG).show();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mock_walk, container, false);
    }

}
