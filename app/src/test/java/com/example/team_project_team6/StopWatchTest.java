package com.example.team_project_team6;

import android.os.Bundle;
import android.widget.TextView;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.StopWatch;
import com.example.team_project_team6.ui.routes.RoutesViewModel;
import com.example.team_project_team6.ui.walk.WalkFragment;
import com.example.team_project_team6.ui.walk.WalkViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
public class StopWatchTest {
    Bundle bundle;
    FragmentFactory factory;
    FragmentScenario<WalkFragment> scenario;
    WalkViewModel walkViewModel;
    TextView tv;


    @Test
    public void stopWatchTest() {
        bundle = new Bundle();
        new FragmentFactory();
        scenario = FragmentScenario.launchInContainer(WalkFragment.class, bundle, R.style.Theme_AppCompat, factory);
        scenario.onFragment(new FragmentScenario.FragmentAction<WalkFragment>() {
            @Override
            public void perform(@NonNull WalkFragment fragment){
                tv = fragment.getView().findViewById(R.id.lbTime);
                //fragment.getView().findViewById(R.id.btStart).performClick();
                //walkViewModel = new ViewModelProvider(fragment).get(WalkViewModel.class);
                    try {
                        //fragment.getView().findViewById(R.id.btStart).performClick();
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                //fragment.getView().findViewById(R.id.btStart).performClick();
                //System.out.println("final: " + tv.getText().toString());
                assertThat(tv.getText().toString()).isEqualTo("00:00:00");
            }
        });

    }
    }
