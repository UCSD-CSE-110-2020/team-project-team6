package com.example.team_project_team6.ui.team;


import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.team_project_team6.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.contrib.PickerActions.setTime;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@LargeTest
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

        onView(withId(R.id.bt_done_set)).perform(click());
        assertEquals(R.id.setProposedDate, navController.getCurrentDestination().getId());
    }

    @Test
    public void TestAllFieldsEntered() {
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

        /** Test code from following link:
         *  https://android.googlesource.com/platform/frameworks/testing/+/android-support-test/espresso/sample/src/androidTest/java/android/support/test/testapp/PickerActionsTest.java
         */
        // Show the date picker
        onView(withId(R.id.date_edit)).perform(click(), click());
        // Sets a date on the date picker widget
        onView(isAssignableFrom(DatePicker.class)).perform(setDate(2020, 3, 10));
        // Confirm the selected date. This example uses a standard DatePickerDialog
        // which uses
        // android.R.id.button1 for the positive button id.
        onView(withId(android.R.id.button1)).perform(click());
        // Check if the selected date is correct and is displayed in the Ui.
        onView(withId(R.id.date_edit)).check(ViewAssertions.matches(allOf(withText("03/10/2020"),
                isDisplayed())));

        // Show the date picker
        onView(withId(R.id.time_edit)).perform(click(), click());
        // Sets a time in a view picker widget
        onView(isAssignableFrom(TimePicker.class)).perform(setTime(12, 36));
        // Confirm the time
        onView(withId(android.R.id.button1)).perform(click());
        // Check if the date result is displayed.
        onView(withId(R.id.time_edit)).check(ViewAssertions.matches(allOf(withText("12:36"),
                isDisplayed())));

        // Check that we navigate to team screen
        onView(withId(R.id.bt_done_set)).perform(click());
        assertEquals(R.id.navigation_team, navController.getCurrentDestination().getId());
    }
}