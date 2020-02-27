package com.example.team_project_team6.ui.new_route;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.SaveData;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class NewRouteFragmentTest {
    @Test
    public void TestCreateRouteMandatory() {
        final TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);
        navController.setCurrentDestination(R.id.newRouteFragment);

        FragmentScenario<NewRouteFragment> scenario = FragmentScenario.launchInContainer(NewRouteFragment.class, null, R.style.Theme_AppCompat, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader,
                                        @NonNull String className) {
                NewRouteFragment fragment = new NewRouteFragment();

                fragment.getViewLifecycleOwnerLiveData().observeForever(lifecycleOwner -> {
                    if (lifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController);
                    }
                });

                return fragment;
            }
        });

        RouteDetailsViewModel viewModel = mock(RouteDetailsViewModel.class);
        when(viewModel.getIsWalkFromRouteDetails()).thenReturn(false);

        NewRouteFragment.routeDetailsViewModel = viewModel;
        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        // verify nothing happens if we click done without filling in the required fields
        onView(ViewMatchers.withId(R.id.btDone)).perform(ViewActions.scrollTo(), ViewActions.click());
        assertEquals(R.id.newRouteFragment, navController.getCurrentDestination().getId());

        // Type and then close keyboard
        onView(ViewMatchers.withId(R.id.txtRouteName)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(ViewMatchers.withId(R.id.txtRouteName)).perform(ViewActions.typeText("ABC"));
        closeSoftKeyboard();

        // click done button and check navigation destination
        onView(ViewMatchers.withId(R.id.btDone)).perform(ViewActions.scrollTo());
        onView(ViewMatchers.withId(R.id.btDone)).perform(ViewActions.click());
        assertEquals(R.id.navigation_routes, navController.getCurrentDestination().getId());

        // Check to see if it was saved correctly
        SaveData data = new SaveData(ApplicationProvider.getApplicationContext());
        assertNotNull(data.getRoute("ABC"));
    }

    @Test
    public void TestCreateRouteOptional() {
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
        closeSoftKeyboard();

        // Pick all optional features
        onView(ViewMatchers.withId(R.id.rdEasy)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(ViewMatchers.withId(R.id.rdFlat)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(ViewMatchers.withId(R.id.rdStreet)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(ViewMatchers.withId(R.id.rdEven)).perform(ViewActions.scrollTo(), ViewActions.click());
        onView(ViewMatchers.withId(R.id.rdLoop)).perform(ViewActions.scrollTo(), ViewActions.click());

        // click done button and check navigation destination
        onView(ViewMatchers.withId(R.id.btDone)).perform(ViewActions.scrollTo());
        onView(ViewMatchers.withId(R.id.btDone)).perform(ViewActions.click());
        assertEquals(R.id.navigation_routes, navController.getCurrentDestination().getId());

        // Check to see if it was saved correctly
        SaveData data = new SaveData(ApplicationProvider.getApplicationContext());
        assertNotNull(data.getRoute("ABC"));
        assertEquals(1, data.getRoute("ABC").getFeatures().getLevel());
        assertEquals(1, data.getRoute("ABC").getFeatures().getType());
        assertEquals(1, data.getRoute("ABC").getFeatures().getSurface());
        assertEquals(1, data.getRoute("ABC").getFeatures().getTerrain());
        assertEquals(1, data.getRoute("ABC").getFeatures().getDirectionType());
    }

    @Test
    public void TestCreateRouteNotes() {
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
                NewRouteFragment.routeDetailsViewModel = viewModel;
            }
        });

        // verify nothing happens if we click done without filling in the required fields
        onView(ViewMatchers.withId(R.id.btDone)).perform(ViewActions.scrollTo(), ViewActions.click());
        assertEquals(R.id.newRouteFragment, navController.getCurrentDestination().getId());

        // Type and then close keyboard
        onView(ViewMatchers.withId(R.id.txtNotes)).perform(ViewActions.scrollTo(), ViewActions.typeText("Lorem Ipsum"), ViewActions.closeSoftKeyboard());

        // Type and then close keyboard
        onView(ViewMatchers.withId(R.id.txtRouteName)).perform(ViewActions.scrollTo(), ViewActions.click(), ViewActions.scrollTo(), ViewActions.typeText("ABC")).perform(ViewActions.closeSoftKeyboard());

        // click done button and check navigation destination
        onView(ViewMatchers.withId(R.id.btDone)).perform(ViewActions.scrollTo());
        onView(ViewMatchers.withId(R.id.btDone)).perform(ViewActions.click());
        assertEquals(R.id.navigation_routes, navController.getCurrentDestination().getId());

        // Check to see if it was saved correctly
        SaveData data = new SaveData(ApplicationProvider.getApplicationContext());
        assertNotNull(data.getRoute("ABC"));
        assertEquals("Lorem Ipsum", data.getRoute("ABC").getNotes());
        assertEquals(0, data.getRoute("ABC").getFeatures().getLevel());
        assertEquals(0, data.getRoute("ABC").getFeatures().getType());
        assertEquals(0, data.getRoute("ABC").getFeatures().getSurface());
        assertEquals(0, data.getRoute("ABC").getFeatures().getTerrain());
        assertEquals(0, data.getRoute("ABC").getFeatures().getDirectionType());
    }
}
