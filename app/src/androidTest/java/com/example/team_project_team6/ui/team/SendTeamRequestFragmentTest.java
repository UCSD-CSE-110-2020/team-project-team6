package com.example.team_project_team6.ui.team;


import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class SendTeamRequestFragmentTest {
    private TestNavHostController navController = null;
    private TeamViewModel viewModel = null;

    @Before
    public void setup() {
        navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);
        navController.setCurrentDestination(R.id.navigation_team);
        viewModel = mock(TeamViewModel.class);
    }

    @Test
    public void TestInviteFormFieldsMandatory() {
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<SendTeamRequestFragment> scenario =
                FragmentScenario.launchInContainer(SendTeamRequestFragment.class, null, R.style.Theme_AppCompat, factory);

        scenario.onFragment(new FragmentScenario.FragmentAction<SendTeamRequestFragment>() {
            @Override
            public void perform(@NonNull SendTeamRequestFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navController);
                fragment.teamViewModel = viewModel;
            }
        });
        // TODO: Test that 'Invite' button takes you back to Team screen if all mandatory fields are filled out


        // TODO: Test otherwise that you stay on the Team screen if there are missing mandatory fields
        // click on invite button without filling in any fields
        onView(ViewMatchers.withId(R.id.bt_invite)).perform(ViewActions.click());
        // check that we stay on the invite form
        assertEquals(R.id.sendTeamRequestFragment, navController.getCurrentDestination().getId());

    }
}
