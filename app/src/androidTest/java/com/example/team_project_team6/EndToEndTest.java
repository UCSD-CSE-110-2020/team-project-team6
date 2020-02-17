package com.example.team_project_team6;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.team_project_team6.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EndToEndTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void endToEndTest() {
        ViewInteraction appCompatButton = onView(
allOf(withId(R.id.btContinue), withText("Continue"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
1),
isDisplayed()));
        appCompatButton.perform(click());
        
        ViewInteraction appCompatButton2 = onView(
allOf(withId(R.id.btContinue), withText("Continue"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
0),
isDisplayed()));
        appCompatButton2.perform(click());
        
        ViewInteraction textView = onView(
allOf(withId(R.id.textDailyDist), withText("-0.00 mi"),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
0),
1),
isDisplayed()));
        textView.check(matches(withText("0.00 mi")));
        
        ViewInteraction textView2 = onView(
allOf(withId(R.id.textDailySteps), withText("0 steps"),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
0),
0),
isDisplayed()));
        textView2.check(matches(withText("0 steps")));
        
        ViewInteraction textView3 = onView(
allOf(withId(R.id.textDailySteps), withText("0 steps"),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
0),
0),
isDisplayed()));
        textView3.check(matches(withText("0 steps")));
        
        ViewInteraction bottomNavigationItemView = onView(
allOf(withId(R.id.navigation_walk), withContentDescription("Walk"),
childAtPosition(
childAtPosition(
withId(R.id.nav_view),
0),
1),
isDisplayed()));
        bottomNavigationItemView.perform(click());
        
        ViewInteraction appCompatButton3 = onView(
allOf(withId(R.id.btStart), withText("START"),
childAtPosition(
childAtPosition(
withId(R.id.nav_host_fragment),
0),
7),
isDisplayed()));
        appCompatButton3.perform(click());
        
        ViewInteraction appCompatButton4 = onView(
allOf(withId(R.id.btStart), withText("STOP"),
childAtPosition(
childAtPosition(
withId(R.id.nav_host_fragment),
0),
7),
isDisplayed()));
        appCompatButton4.perform(click());
        
        ViewInteraction appCompatEditText = onView(
allOf(withId(R.id.txtRouteName),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
1)));
        appCompatEditText.perform(scrollTo(), replaceText("Mission Bay"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText2 = onView(
allOf(withId(R.id.txtNotes),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
10)));
        appCompatEditText2.perform(scrollTo(), longClick());
        
        ViewInteraction appCompatButton5 = onView(
allOf(withId(R.id.btDone), withText("DONE"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
11)));
        appCompatButton5.perform(scrollTo(), click());
        
        ViewInteraction textView4 = onView(
allOf(withId(R.id.item_view_routename), withText("Mission Bay "),
childAtPosition(
childAtPosition(
withId(R.id.tableLayout),
0),
0),
isDisplayed()));
        textView4.check(matches(withText("Mission Bay ")));
        
        ViewInteraction frameLayout = onView(
allOf(childAtPosition(
allOf(withId(R.id.recycler_view_routes),
childAtPosition(
withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
1)),
0),
isDisplayed()));
        frameLayout.perform(click());
        
        ViewInteraction textView5 = onView(
allOf(withText("Mission Bay"),
childAtPosition(
allOf(withId(R.id.action_bar),
childAtPosition(
withId(R.id.action_bar_container),
0)),
1),
isDisplayed()));
        textView5.check(matches(withText("Mission Bay")));
        }
    
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
