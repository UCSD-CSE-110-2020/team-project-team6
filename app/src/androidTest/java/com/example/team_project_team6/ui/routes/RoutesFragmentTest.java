package com.example.team_project_team6.ui.routes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.TeamMember;
import com.example.team_project_team6.model.Walk;
import com.example.team_project_team6.ui.route_details.RouteDetailsViewModel;
import com.google.android.material.tabs.TabLayout;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Objects;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class RoutesFragmentTest {
    @Test
    public void TestNoTeammates() {
        final TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);
        navController.setCurrentDestination(R.id.newRouteFragment);

        RouteDetailsViewModel routeDetailsViewModel = mock(RouteDetailsViewModel.class);
        RoutesViewModel routesViewModel = mock(RoutesViewModel.class);

        MutableLiveData<ArrayList<Route>> teamRoutes = new MutableLiveData<>(new ArrayList<>());
        when(routesViewModel.getRouteData()).thenReturn(teamRoutes);

        FragmentScenario<RoutesFragment> scenario = FragmentScenario.launchInContainer(RoutesFragment.class, null, R.style.Theme_AppCompat, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader,
                                        @NonNull String className) {
                RoutesFragment fragment = new RoutesFragment();

                fragment.getViewLifecycleOwnerLiveData().observeForever(lifecycleOwner -> {
                    if (lifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController);
                    }
                });

                fragment.routesViewModel = routesViewModel;
                fragment.routeDetailsViewModel = routeDetailsViewModel;

                return fragment;
            }
        });

        onView(ViewMatchers.withId(R.id.routeTabLayout)).perform(selectTabAtPosition(1), click());
        onView(ViewMatchers.withId(R.id.recycler_view_routes)).check(new RecyclerViewItemCountAssertion(0));
    }

    @Test
    public void TestTeammatesNoRoutes() {
        final TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);
        navController.setCurrentDestination(R.id.newRouteFragment);

        RouteDetailsViewModel routeDetailsViewModel = mock(RouteDetailsViewModel.class);
        RoutesViewModel routesViewModel = mock(RoutesViewModel.class);

        MutableLiveData<ArrayList<Route>> teamRoutes = new MutableLiveData<>(new ArrayList<>());
        when(routesViewModel.getRouteData()).thenReturn(teamRoutes);

        FragmentScenario<RoutesFragment> scenario = FragmentScenario.launchInContainer(RoutesFragment.class, null, R.style.Theme_AppCompat, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader,
                                        @NonNull String className) {
                RoutesFragment fragment = new RoutesFragment();

                fragment.getViewLifecycleOwnerLiveData().observeForever(lifecycleOwner -> {
                    if (lifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController);
                    }
                });

                fragment.routesViewModel = routesViewModel;
                fragment.routeDetailsViewModel = routeDetailsViewModel;

                return fragment;
            }
        });

        onView(ViewMatchers.withId(R.id.routeTabLayout)).perform(selectTabAtPosition(1), click());
        onView(ViewMatchers.withId(R.id.recycler_view_routes)).check(new RecyclerViewItemCountAssertion(0));
    }

    @NonNull
    private static ViewAction selectTabAtPosition(final int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "with tab at index" + String.valueOf(position);
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view instanceof TabLayout) {
                    TabLayout tabLayout = (TabLayout) view;
                    TabLayout.Tab tab = tabLayout.getTabAt(position);

                    if (tab != null) {
                        tab.select();
                    }
                }
            }
        };
    }

    @Test
    public void TestTeamatesAndRoutes() {
        final TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.mobile_navigation);
        navController.setCurrentDestination(R.id.newRouteFragment);

        RouteDetailsViewModel routeDetailsViewModel = mock(RouteDetailsViewModel.class);
        RoutesViewModel routesViewModel = mock(RoutesViewModel.class);

        MutableLiveData<ArrayList<Route>> teamRoutes = new MutableLiveData<>(new ArrayList<>());
        when(routesViewModel.getRouteData()).thenReturn(teamRoutes);

        FragmentScenario<RoutesFragment> scenario = FragmentScenario.launchInContainer(RoutesFragment.class, null, R.style.Theme_AppCompat, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader,
                                        @NonNull String className) {
                RoutesFragment fragment = new RoutesFragment();

                fragment.getViewLifecycleOwnerLiveData().observeForever(lifecycleOwner -> {
                    if (lifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController);
                    }
                });

                fragment.routesViewModel = routesViewModel;
                fragment.routeDetailsViewModel = routeDetailsViewModel;

                return fragment;
            }
        });

        ArrayList<Route> routes = new ArrayList<>();
        TeamMember owner = new TeamMember("example@example.com", "E", "C");
        Walk walk = new Walk();
        Features f = new Features();
        routes.add(new Route(walk, "University of California, San Diego, EBU3B", null, "Test Walk Notes", f, "Mission Bay", "wsh", owner));

        owner = new TeamMember("abc@def.com", "F", "G");
        routes.add(new Route(walk, "FooBar", null, "Test Walk Notes", f, "Mission Bay", "def", owner));

        teamRoutes.postValue(routes);

        onView(ViewMatchers.withId(R.id.routeTabLayout)).perform(selectTabAtPosition(1));
        onView(ViewMatchers.withId(R.id.recycler_view_routes)).check(new RecyclerViewItemCountAssertion(2));

        onView(ViewMatchers.withId(R.id.routeTabLayout)).perform(selectTabAtPosition(1));
        onView(ViewMatchers.withId(R.id.recycler_view_routes)).check(new RecyclerViewItemInitialsVisible());
    }

    public static class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assert adapter != null;
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

    public static class RecyclerViewItemInitialsVisible implements ViewAssertion {
        RecyclerViewItemInitialsVisible() {
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assert adapter != null;
            TextView initials = (TextView)(Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(0)).itemView.findViewById(R.id.item_view_initials));
            assertThat(initials.getText(), is("wsh"));
            assertThat(initials.getVisibility(), is(View.VISIBLE));
        }
    }
}
