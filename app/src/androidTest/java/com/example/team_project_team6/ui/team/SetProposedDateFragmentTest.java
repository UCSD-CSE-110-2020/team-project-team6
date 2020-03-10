package com.example.team_project_team6.ui.team;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team_project_team6.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class SetProposedDateFragmentTest {
    private TestNavHostController navController = null;
    private TeamViewModel viewModel = null;

    @Before
    public void setup() {
        navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);
        navController.setCurrentDestination(R.id.setProposedDate);
        viewModel = mock(TeamViewModel.class);
    }

    @Test
    public void TestMandatoryFields() {
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<SetProposedDateFragment> scenario =
                FragmentScenario.launchInContainer(SetProposedDateFragment.class, null, R.style.Theme_AppCompat, factory);

        scenario.onFragment(new FragmentScenario.FragmentAction<SetProposedDateFragment>() {
            @Override
            public void perform(@NonNull SetProposedDateFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navController);
                fragment.teamViewModel = viewModel;
            }
        });


        onView(ViewMatchers.withId(R.id.bt_done_set)).perform(ViewActions.click());

        assertEquals(R.id.setProposedDate, navController.getCurrentDestination().getId());
    }


}