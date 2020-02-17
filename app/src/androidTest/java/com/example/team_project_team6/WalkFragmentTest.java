package com.example.team_project_team6;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.ui.new_route.NewRouteFragment;
import com.example.team_project_team6.ui.route_details.RouteDetailsFragment;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;
import com.example.team_project_team6.ui.walk.WalkFragment;
import com.example.team_project_team6.ui.walk.WalkViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class WalkFragmentTest {
    @Test
    public void TestCreateRouteFromNewWalk() {
        final TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);

        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<WalkFragment> scenario =
                FragmentScenario.launchInContainer(WalkFragment.class, null, R.style.Theme_AppCompat, factory);

        WalkViewModel viewModel = mock(WalkViewModel.class);
        MutableLiveData<Boolean> boolLiveData = new MutableLiveData<>();
        MutableLiveData<String> stopwatchLiveData = new MutableLiveData<>();
        MutableLiveData<Long> stepsLiveData = new MutableLiveData<>();

        boolLiveData.postValue(false);
        stopwatchLiveData.postValue("00:00:00");
        stepsLiveData.postValue(0L);

        when(viewModel.getIsMockWalk()).thenReturn(false);
        when(viewModel.isCurrentlyWalking()).thenReturn(boolLiveData);
        when(viewModel.getStopWatch()).thenReturn(stopwatchLiveData);
        when(viewModel.getWalkSteps()).thenReturn(stepsLiveData);
        doNothing().when(viewModel).startWalking();
        doNothing().when(viewModel).endWalking();
        when(viewModel.isWalking()).thenReturn(true);

        scenario.onFragment(new FragmentScenario.FragmentAction<WalkFragment>() {
            @Override
            public void perform(@NonNull WalkFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navController);
                fragment.walkViewModel = viewModel;
            }
        });

        // click on start button
        onView(ViewMatchers.withId(R.id.btStart)).perform(ViewActions.click());
        assertTrue(viewModel.isWalking());
    }

    @Test
    public void StartWalkFromRouteTest() {
        final TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);

        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<WalkFragment> scenarioW =
                FragmentScenario.launchInContainer(WalkFragment.class, null, R.style.Theme_AppCompat, factory);
        FragmentScenario<RouteDetailsFragment> scenarioR =
                FragmentScenario.launchInContainer(RouteDetailsFragment.class, null, R.style.Theme_AppCompat, factory);

        WalkViewModel wViewModel = mock(WalkViewModel.class);
        MutableLiveData<Boolean> boolLiveData = new MutableLiveData<>();
        MutableLiveData<String> stopwatchLiveData = new MutableLiveData<>();
        MutableLiveData<Long> stepsLiveData = new MutableLiveData<>();

        boolLiveData.postValue(false);
        stopwatchLiveData.postValue("00:00:00");
        stepsLiveData.postValue(0L);

        when(wViewModel.getIsMockWalk()).thenReturn(false);
        when(wViewModel.isCurrentlyWalking()).thenReturn(boolLiveData);
        when(wViewModel.getStopWatch()).thenReturn(stopwatchLiveData);
        when(wViewModel.getWalkSteps()).thenReturn(stepsLiveData);
        doNothing().when(wViewModel).startWalking();
        doNothing().when(wViewModel).endWalking();
        when(wViewModel.isWalking()).thenReturn(true);

        RouteDetailsViewModel rViewModel = mock(RouteDetailsViewModel.class);
        when(rViewModel.getRoute()).thenReturn(new Route(null, "University of California, San Diego, EBU3B", null, "Test Walk Notes", null, "Wild Area"));
        doNothing().when(rViewModel).setRoute(any());
        when(rViewModel.getIsWalkFromRouteDetails()).thenReturn(true);
        doNothing().when(rViewModel).setIsWalkFromRouteDetails(anyBoolean());

        scenarioR.onFragment(new FragmentScenario.FragmentAction<RouteDetailsFragment>() {
            @Override
            public void perform(@NonNull RouteDetailsFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navController);
                fragment.mViewModel = rViewModel;
            }
        });

        scenarioW.onFragment(new FragmentScenario.FragmentAction<WalkFragment>() {
            @Override
            public void perform(@NonNull WalkFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navController);
                fragment.walkViewModel = wViewModel;
            }
        });
    }

    @Test
    public void NoNewWalkOnCurrentWalkTest() {

    }

    @Test
    public void StopWalkFromWalkTabTest() {

    }

    @Test
    public void StopWalkFromRouteTest() {

    }
}
