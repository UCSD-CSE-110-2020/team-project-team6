package com.example.team_project_team6.ui.team;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team_project_team6.R;
import com.example.team_project_team6.firebase.FirebaseGoogleAdapter;
import com.example.team_project_team6.firebase.IFirebase;
import com.example.team_project_team6.model.SaveData;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

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
        saveData.acceptTeamRequest();

        onView(withId(R.id.bt_acceptWalk)).perform(setButtonVisibility(true));
        onView(withId(R.id.bt_declineTime)).perform(setButtonVisibility(true));
        onView(withId(R.id.bt_declineRoute)).perform(setButtonVisibility(true));

        onView(ViewMatchers.withId(R.id.bt_acceptWalk)).perform(nestedScrollTo(), ViewActions.click());
        //onView(ViewMatchers.withId(R.id.bt_acceptWalk)).perform(ViewActions.click());
        assertEquals(true, viewModel.getInviteIsAccepted());

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