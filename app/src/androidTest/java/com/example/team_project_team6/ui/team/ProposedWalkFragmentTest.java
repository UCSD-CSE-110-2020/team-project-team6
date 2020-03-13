package com.example.team_project_team6.ui.team;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.util.Checks;

import com.example.team_project_team6.R;
import com.example.team_project_team6.firebase.FirebaseGoogleAdapter;
import com.example.team_project_team6.firebase.IFirebase;
import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.ProposedWalk;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.model.TeamMember;
import com.example.team_project_team6.model.Walk;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ProposedWalkFragmentTest {
    private TestNavHostController navController = null;
    private TeamViewModel viewModel = null;

    @Mock private Context context;
    @Mock private IFirebase adapter;
    private SaveData saveData;

    @Before
    public void setup() {
        navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);
        navController.setCurrentDestination(R.id.proposedWalkFragment);
        viewModel = mock(TeamViewModel.class);

        this.context = Mockito.mock(Context.class);
        this.adapter = Mockito.mock(FirebaseGoogleAdapter.class);
        saveData = new SaveData(context, adapter);
    }

    @Test
    public void TestIsAcceptedDeclineVisibility() {
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<ProposedWalkFragment> scenario =
                FragmentScenario.launchInContainer(ProposedWalkFragment.class, null, R.style.Theme_AppCompat, factory);

        ProposedWalk proposedWalk = new ProposedWalk();
        Walk walk = new Walk();
        Date date = new Date();
        Features features = new Features();
        String startPoint = "start point";
        String name = "Name";
        String notes = "";
        String initials = "NI";
        TeamMember owner = new TeamMember("ycxing99@gmail.com", "Cora", "Xing");
        proposedWalk.setpRoute(new Route(walk, startPoint, date, notes, features, name, initials, owner));
        proposedWalk.setpDayMonthYearDate("03/12/2020");
        proposedWalk.setpHourSecondTime("14:48:00");

        MutableLiveData<ProposedWalk> proposedWalks = new MutableLiveData<>(new ProposedWalk());
        when(viewModel.getProposedWalkData()).thenReturn(proposedWalks);

        scenario.onFragment(new FragmentScenario.FragmentAction<ProposedWalkFragment>() {
            @Override
            public void perform(@NonNull ProposedWalkFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navController);
                fragment.teamViewModel = viewModel;
                //fragment.getView().findViewById((R.id.txt_proposed_route_Name)).s;
            }
        });

        Mockito.when(viewModel.isMyProposedWalk()).thenReturn(false);
        saveData.acceptTeamRequest();

        onView(withId(R.id.bt_acceptWalk)).perform(setButtonVisibility(true));
        onView(withId(R.id.bt_declineTime)).perform(setButtonVisibility(true));
        onView(withId(R.id.bt_declineRoute)).perform(setButtonVisibility(true));

        onView(ViewMatchers.withId(R.id.bt_acceptWalk)).perform(nestedScrollTo(), ViewActions.click());

        onView(withId(R.id.bt_Schedule)).check(ViewAssertions.matches(not(isDisplayed())));
        onView(withId(R.id.bt_withdraw)).check(ViewAssertions.matches(not(isDisplayed())));
    }

    public static ViewAction nestedScrollTo() {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return Matchers.allOf(
                        isDescendantOfA(isAssignableFrom(NestedScrollView.class)),
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE));
            }

            @Override
            public String getDescription() {
                return "View is not NestedScrollView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                try {
                    NestedScrollView nestedScrollView = (NestedScrollView)
                            findFirstParentLayoutOfClass(view, NestedScrollView.class);
                    if (nestedScrollView != null) {
                        nestedScrollView.scrollTo(0, view.getTop());
                    } else {
                        throw new Exception("Unable to find NestedScrollView parent.");
                    }
                } catch (Exception e) {
                    throw new PerformException.Builder()
                            .withActionDescription(this.getDescription())
                            .withViewDescription(HumanReadables.describe(view))
                            .withCause(e)
                            .build();
                }
                uiController.loopMainThreadUntilIdle();
            }

        };
    }

    private static View findFirstParentLayoutOfClass(View view, Class<? extends View> parentClass) {
        ViewParent parent = new FrameLayout(view.getContext());
        ViewParent incrementView = null;
        int i = 0;
        while (parent != null && !(parent.getClass() == parentClass)) {
            if (i == 0) {
                parent = findParent(view);
            } else {
                parent = findParent(incrementView);
            }
            incrementView = parent;
            i++;
        }
        return (View) parent;
    }

    private static ViewParent findParent(View view) {
        return view.getParent();
    }

    private static ViewParent findParent(ViewParent view) {
        return view.getParent();
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