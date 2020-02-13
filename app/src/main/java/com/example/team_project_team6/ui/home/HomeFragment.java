package com.example.team_project_team6.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;
import com.example.team_project_team6.model.SaveData;

import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentActivity mActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mActivity = requireActivity();
        final MainActivity mainActivity = (MainActivity) getActivity();
        final SaveData saveData = new SaveData(mainActivity);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView dailySteps = root.findViewById(R.id.textDailySteps);
        final TextView dailyDist = root.findViewById(R.id.textDailyDist);
        homeViewModel.getDailySteps().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long num) {

                final int heightInInches = saveData.getHeight();
                final double strideDistInFt = (0.413 * (double) heightInInches) / 12.0;
                Log.i("height", String.format(Locale.ENGLISH, "height: %d, distance: %f", heightInInches, strideDistInFt));

                if (num == null) {
                    num = 0L;
                }

                double dist = strideDistInFt * num / 5280.0;

                dailySteps.setText(String.format(Locale.ENGLISH, "%d steps", num));
                dailyDist.setText(String.format(Locale.ENGLISH, "%.2f mi", dist));
            }
        });

        return root;
    }
}