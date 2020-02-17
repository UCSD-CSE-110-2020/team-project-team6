package com.example.team_project_team6;

import android.view.KeyEvent;

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

import com.example.team_project_team6.ui.new_route.NewRouteFragment;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;
import com.example.team_project_team6.ui.walk.WalkFragment;
import com.example.team_project_team6.ui.walk.WalkViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static org.junit.Assert.assertEquals;
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
        boolean isWalking = doCallRealMethod().when(viewModel).isWalking();

        scenario.onFragment(new FragmentScenario.FragmentAction<WalkFragment>() {
            @Override
            public void perform(@NonNull WalkFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navController);
                fragment.walkViewModel = viewModel;
            }
        });

        // verify nothing happens if we click done without filling in the required fields
        onView(ViewMatchers.withId(R.id.btStart)).perform(ViewActions.click());
//        onView(ViewMatchers.withId(R.id.btStart)).perform(ViewActions.scrollTo(), ViewActions.click());
        assertEquals(true, viewModel.isWalking());
    }
}
