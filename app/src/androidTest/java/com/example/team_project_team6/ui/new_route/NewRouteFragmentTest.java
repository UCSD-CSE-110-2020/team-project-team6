package com.example.team_project_team6.ui.new_route;

import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.KeyEventAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team_project_team6.R;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class NewRouteFragmentTest {
    @Test
    public void TestCreateRouteFromNewWalk() {
        final TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);
        navController.setCurrentDestination(R.id.newRouteFragment);

        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<NewRouteFragment> scenario =
                FragmentScenario.launchInContainer(NewRouteFragment.class, null, R.style.Theme_AppCompat, factory);

        RouteDetailsViewModel viewModel = mock(RouteDetailsViewModel.class);
        when(viewModel.getIsWalkFromRouteDetails()).thenReturn(false);

        scenario.onFragment(new FragmentScenario.FragmentAction<NewRouteFragment>() {
            @Override
            public void perform(@NonNull NewRouteFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navController);
                fragment.routeDetailsViewModel = viewModel;
            }
        });

        // verify nothing happens if we click done without filling in the required fields
        onView(ViewMatchers.withId(R.id.btDone)).perform(ViewActions.scrollTo(), ViewActions.click());
        assertEquals(R.id.newRouteFragment, navController.getCurrentDestination().getId());

        // Type and then close keyboard
        onView(ViewMatchers.withId(R.id.txtRouteName)).perform(ViewActions.scrollTo(), ViewActions.typeText("ABC"));
        Espresso.closeSoftKeyboard();

        // click done button and check navigation destination
        onView(ViewMatchers.withId(R.id.btDone)).perform(ViewActions.scrollTo());
        onView(ViewMatchers.withId(R.id.btDone)).perform(ViewActions.click());
        assertEquals(R.id.navigation_routes, navController.getCurrentDestination().getId());
    }
}
