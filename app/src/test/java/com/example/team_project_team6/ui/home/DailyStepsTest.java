package com.example.team_project_team6.ui.home;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.ui.routes.RoutesFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class DailyStepsTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void TestNotMoved() {
        Bundle bundle = new Bundle();
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class, bundle, R.style.Theme_AppCompat, factory);

        MutableLiveData<Long> mockSteps = new MutableLiveData<>(0L);
        final HomeViewModel mockedViewModel = mock(HomeViewModel.class);
        when(mockedViewModel.getDailySteps()).thenReturn(mockSteps);

        scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
            @Override
            public void perform(@NonNull HomeFragment fragment) {
                fragment.homeViewModel = mockedViewModel;
                fragment.strideDistInFt = (0.413 * 72) / 12.0;
                fragment.bindViews();

                TextView dailySteps = fragment.requireView().findViewById(R.id.textDailySteps);
                TextView dailyDist = fragment.requireView().findViewById(R.id.textDailyDist);

                mockSteps.postValue(0L);

                assertEquals("0 steps", dailySteps.getText());
                assertEquals("0.00 mi", dailyDist.getText());
            }
        });
    }

    @Test
    public void TestCurrentlyStationary() {
        Bundle bundle = new Bundle();
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class, bundle, R.style.Theme_AppCompat, factory);

        MutableLiveData<Long> mockSteps = new MutableLiveData<>(0L);
        final HomeViewModel mockedViewModel = mock(HomeViewModel.class);
        when(mockedViewModel.getDailySteps()).thenReturn(mockSteps);

        scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
            @Override
            public void perform(@NonNull HomeFragment fragment) {
                fragment.homeViewModel = mockedViewModel;
                fragment.strideDistInFt = (0.413 * 72) / 12.0;
                fragment.bindViews();

                TextView dailySteps = fragment.requireView().findViewById(R.id.textDailySteps);
                TextView dailyDist = fragment.requireView().findViewById(R.id.textDailyDist);

                mockSteps.postValue(100L);
                assertEquals("100 steps", dailySteps.getText());
                assertEquals("0.05 mi", dailyDist.getText());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                assertEquals("100 steps", dailySteps.getText());
                assertEquals("0.05 mi", dailyDist.getText());
            }
        });
    }
}