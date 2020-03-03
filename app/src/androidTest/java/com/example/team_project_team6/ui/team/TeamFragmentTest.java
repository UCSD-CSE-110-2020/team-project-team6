package com.example.team_project_team6.ui.team;


import android.view.View;
import android.widget.Button;

import com.example.team_project_team6.R;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class TeamFragmentTest {
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
    public void TestOnAcceptDenyButtonClickButtonsDisappear() {
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<MemberFragment> scenario =
                FragmentScenario.launchInContainer(MemberFragment.class, null, R.style.Theme_AppCompat, factory);

        // when(viewModel.getHasPendingTeamInvite()).thenReturn(true);
        scenario.onFragment(new FragmentScenario.FragmentAction<MemberFragment>() {
            @Override
            public void perform(@NonNull MemberFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navController);
                fragment.teamViewModel = viewModel;
            }
        });
        onView(withId(R.id.btn_accept_invite)).perform(setButtonVisibility(true));
        onView(withId(R.id.btn_decline_invite)).perform(setButtonVisibility(true));

        // accept and decline buttons should be displayed
        onView(withId(R.id.btn_accept_invite)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.btn_decline_invite)).check(ViewAssertions.matches(isDisplayed()));

        // click accept invite button
        onView(ViewMatchers.withId(R.id.btn_accept_invite)).perform(ViewActions.click());
        // user should no longer have pending team invite
        assertEquals(viewModel.getHasPendingTeamInvite(), false);
        // accept and decline buttons should not be displayed
        onView(withId(R.id.btn_accept_invite)).check(ViewAssertions.matches(not(isDisplayed())));
        onView(withId(R.id.btn_decline_invite)).check(ViewAssertions.matches(not(isDisplayed())));
    }

    @Test
    public void TestTransitionToInviteForm() {
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<TeamFragment> scenario =
                FragmentScenario.launchInContainer(TeamFragment.class, null, R.style.Theme_AppCompat, factory);

        scenario.onFragment(new FragmentScenario.FragmentAction<TeamFragment>() {
            @Override
            public void perform(@NonNull TeamFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navController);
                fragment.teamViewModel = viewModel;
            }
        });

        // click '+' button
        onView(ViewMatchers.withId(R.id.bt_invite_member)).perform(ViewActions.click());
        // check that we transition to invite form
        assertEquals(R.id.sendTeamRequestFragment, navController.getCurrentDestination().getId());
    }

    private static ViewAction setButtonVisibility(final boolean value) {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(Button.class);
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.setVisibility(value ? View.VISIBLE : View.GONE);
            }

            @Override
            public String getDescription() {
                return "Show / Hide View";
            }
        };
    }
}
