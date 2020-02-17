package com.example.team_project_team6;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team_project_team6.ui.walk.WalkFragment;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;

@RunWith(AndroidJUnit4.class)
public class WalkFragTest {

    @Test
    public void StartWalkFromWalkTabTest() {

        // Create a TestNavHostController
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);

        // Create a graphical FragmentScenario for the TitleScreen
        FragmentScenario<WalkFragment> titleScenario = FragmentScenario.launchInContainer(WalkFragment.class);

        // Set the NavController property on the fragment
        titleScenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), navController)
        );

        // Verify that performing a click changes the NavController’s state
        onView(ViewMatchers.withId(R.id.btStart)).perform(ViewActions.click());
    }
}
