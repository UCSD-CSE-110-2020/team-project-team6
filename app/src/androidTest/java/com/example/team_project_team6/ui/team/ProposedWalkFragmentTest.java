package com.example.team_project_team6.ui.team;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team_project_team6.R;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class ProposedWalkFragmentTest {
    private TestNavHostController navController = null;
    private TeamViewModel viewModel = null;

    @Before
    public void setup() {
        navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);
        navController.setCurrentDestination(R.id.proposedWalkFragment);
        viewModel = mock(TeamViewModel.class);
    }

    @Test
    public void TestIsAccepted() {
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<ProposedWalkFragment> scenario =
                FragmentScenario.launchInContainer(ProposedWalkFragment.class, null, R.style.Theme_AppCompat, factory);

        scenario.onFragment(new FragmentScenario.FragmentAction<ProposedWalkFragment>() {
            @Override
            public void perform(@NonNull ProposedWalkFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navController);
                fragment.teamViewModel = viewModel;
                //fragment.getView().findViewById((R.id.txt_proposed_route_Name)).s;
            }
        });


//        onView(ViewMatchers.withId(R.id.txt_proposed_route_Name)).perform(ViewActions.replaceText("Park park"));
//        onView(ViewMatchers.withId(R.id.txt_proposed_steps)).perform(ViewActions.replaceText("1000"), ViewActions.closeSoftKeyboard());
//        onView(ViewMatchers.withId(R.id.txt_proposed_mile)).perform(ViewActions.replaceText("6"), ViewActions.closeSoftKeyboard());
//        onView(ViewMatchers.withId(R.id.txt_proposed_date)).perform(ViewActions.replaceText("Park park"), ViewActions.closeSoftKeyboard());
//        onView(ViewMatchers.withId(R.id.txt_proposed_time)).perform(ViewActions.replaceText("6:10"), ViewActions.closeSoftKeyboard());
//        onView(ViewMatchers.withId(R.id.txt_proposed_startingPoint)).perform(ViewActions.replaceText("home"), ViewActions.closeSoftKeyboard());
//        // onView(ViewMatchers.withId(R.id.list_is_going)).perform(ViewActions.replaceText("Park park"), ViewActions.closeSoftKeyboard());
        Mockito.when(viewModel.isMyProposedWalk()).thenReturn(false);
        onView(withId(R.id.bt_acceptWalk)).perform(setButtonVisibility(true));
        onView(withId(R.id.bt_declineTime)).perform(setButtonVisibility(true));
        onView(withId(R.id.bt_declineRoute)).perform(setButtonVisibility(true));

        onView(ViewMatchers.withId(R.id.bt_acceptWalk)).perform(ViewActions.scrollTo(), ViewActions.click());
        //onView(ViewMatchers.withId(R.id.bt_acceptWalk)).perform(ViewActions.click());
        assertEquals(true, viewModel.getInviteIsAccepted());

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