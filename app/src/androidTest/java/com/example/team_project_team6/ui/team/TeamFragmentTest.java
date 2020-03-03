package com.example.team_project_team6.ui.team;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;

import com.example.team_project_team6.MainActivity;
import com.example.team_project_team6.R;
import com.example.team_project_team6.ui.new_route.NewRouteFragment;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;

import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.matcher.VisibilityMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class TeamFragmentTest {

    @Test
    public void TestOnAcceptDenyButtonClickButtonsDisappear() {
        final TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);
        navController.setCurrentDestination(R.id.navigation_team);

        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<MemberFragment> scenario =
                FragmentScenario.launchInContainer(MemberFragment.class, null, R.style.Theme_AppCompat, factory);

        TeamViewModel viewModel = mock(TeamViewModel.class);
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
        // TODO: Test that '+' button takes you to invite form
    }

    @Test
    public void TestInviteFormFieldsMandatory() {
        // TODO: Test that 'Invite' button takes you back to Team screen if all mandatory fields are filled out
        // TODO: Test otherwise that you stay on the Team screen if there are missing mandatory fields
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
