package com.example.team_project_team6;

import android.os.Bundle;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.team_project_team6.ui.walk.WalkFragment;
import com.example.team_project_team6.ui.walk.WalkViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static androidx.test.espresso.Espresso.onView;


@RunWith(AndroidJUnit4.class)
public class WalkFragmentTestOLD {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void StartWalkFromWalkTabTest() {

        final TestNavHostController navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.mobile_navigation);

        Bundle bundle = new Bundle();
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<WalkFragment> scenario = FragmentScenario.launchInContainer(WalkFragment.class, bundle, R.style.Theme_AppCompat, factory);

        scenario.onFragment(fragment -> {
                    Navigation.setViewNavController(fragment.requireView(), navHostController);
                }
        );
        onView(ViewMatchers.withId(R.id.btStart)).perform(ViewActions.click());

//        scenario.onFragment(new FragmentScenario.FragmentAction<WalkFragment>() {
//            @Override
//            public void perform(@NonNull WalkFragment fragment) {
//                Navigation.setViewNavController(fragment.requireView(), navHostController);
//                fragment.walkViewModel = mWalkVM;
//                fragment.routeDetailsViewModel = mRouteDetailsVM;
//
//                Button startBtn = fragment.getView().findViewById(R.id.btStart);
//
//
//
//                assertEquals(0, 0);
//            }
//        });
    }

    @Test
    public void StartWalkFromRouteTest() {

    }

    @Test
    public void NoNewWalkOnCurrentWalkTest() {

    }

    @Test
    public void StopWalkFromWalkTabTest() {
        final TestNavHostController navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.mobile_navigation);

        Bundle bundle = new Bundle();
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<WalkFragment> scenario = FragmentScenario.launchInContainer(WalkFragment.class, bundle, R.style.Theme_AppCompat, factory);
        final WalkViewModel mockedViewModel = Mockito.mock(WalkViewModel.class);
        Mockito.when(mockedViewModel.getStopWatch()).thenReturn(new MutableLiveData<>());

    }

    @Test
    public void StopWalkFromRouteTest() {

    }


/*    @Test
    public void NoSavedRoutesTest() {
        ArrayList< Route > data = new ArrayList<Route>();

        final TestNavHostController navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.mobile_navigation);

        Bundle bundle = new Bundle();
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<RoutesFragment> scenario = FragmentScenario.launchInContainer(RoutesFragment.class, bundle, R.style.Theme_AppCompat, factory);
        final RoutesViewModel mockedViewModel = mock(RoutesViewModel.class);
        when(mockedViewModel.getRouteData()).thenReturn(new MutableLiveData<>(data));

        scenario.onFragment(new FragmentScenario.FragmentAction<RoutesFragment>() {
            @Override
            public void perform(@NonNull RoutesFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navHostController);
                fragment.routesViewModel = mockedViewModel;

                fragment.bind_views();

                RecyclerView recycler = fragment.getView().findViewById(R.id.recycler_view_routes);
                assertEquals(0, recycler.getAdapter().getItemCount());
            }
        });
    }

    @Test
    public void SingleRouteTest() {
        Walk w = new Walk();
        w.setDist(123.456);
        w.setStep(612);
        w.setDuration("00:24:11");
        w.setStartTime(Calendar.getInstance());
        Features f = new Features();
        f.setDirectionType(1);
        f.setLevel(1);
        f.setSurface(1);
        f.setTerrain(1);
        f.setType(1);
        Route route = new Route(w, "University of California, San Diego, EBU3B", null, "Test Walk Notes", f, "Mission Bay");
        ArrayList< Route > data = new ArrayList<Route>();
        data.add(route);

        final TestNavHostController navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.mobile_navigation);

        Bundle bundle = new Bundle();
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<RoutesFragment> scenario = FragmentScenario.launchInContainer(RoutesFragment.class, bundle, R.style.Theme_AppCompat, factory);
        final RoutesViewModel mockedViewModel = mock(RoutesViewModel.class);
        when(mockedViewModel.getRouteData()).thenReturn(new MutableLiveData<>(data));

        scenario.onFragment(new FragmentScenario.FragmentAction<RoutesFragment>() {
            @Override
            public void perform(@NonNull RoutesFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navHostController);
                fragment.routesViewModel = mockedViewModel;

                fragment.bind_views();

                RecyclerView recycler = fragment.getView().findViewById(R.id.recycler_view_routes);
                assertEquals(1, recycler.getAdapter().getItemCount());
            }
        });
    }

    @Test
    public void DisplaySameOrderTest() {
        Route mission = new Route(new Walk(), "", null, "", new Features(), "Mission Bay");
        Route aardvark_park = new Route(new Walk(), "", null, "", new Features(), "Aardvark Park");
        ArrayList< Route > data = new ArrayList<Route>();
        data.add(aardvark_park);
        data.add(mission);

        final TestNavHostController navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.mobile_navigation);

        Bundle bundle = new Bundle();
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<RoutesFragment> scenario = FragmentScenario.launchInContainer(RoutesFragment.class, bundle, R.style.Theme_AppCompat, factory);
        final RoutesViewModel mockedViewModel = mock(RoutesViewModel.class);
        when(mockedViewModel.getRouteData()).thenReturn(new MutableLiveData<>(data));

        scenario.onFragment(new FragmentScenario.FragmentAction<RoutesFragment>() {
            @Override
            public void perform(@NonNull RoutesFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navHostController);
                fragment.routesViewModel = mockedViewModel;
                fragment.bind_views();

                RecyclerView recycler = fragment.getView().findViewById(R.id.recycler_view_routes);
                assertEquals(2, recycler.getAdapter().getItemCount());

                View item = recycler.getLayoutManager().findViewByPosition(0);
                TextView name = item.findViewById(R.id.item_view_routename);
                assertEquals("Aardvark Park", name.getText());

                item = recycler.getLayoutManager().findViewByPosition(1);
                name = item.findViewById(R.id.item_view_routename);
                assertEquals("Mission Bay", name.getText());
            }
        });
    }

    @Test
    public void UpdateDataTest() {
        final Route mission = new Route(new Walk(), "", null, "", new Features(), "Mission Bay");
        final Route aardvark_park = new Route(new Walk(), "", null, "", new Features(), "Aardvark Park");
        final ArrayList< Route > data = new ArrayList<Route>();
        data.add(mission);

        final TestNavHostController navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.mobile_navigation);

        Bundle bundle = new Bundle();
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<RoutesFragment> scenario = FragmentScenario.launchInContainer(RoutesFragment.class, bundle, R.style.Theme_AppCompat, factory);
        final RoutesViewModel mockedViewModel = mock(RoutesViewModel.class);
        final MutableLiveData<ArrayList<Route>> observed_data = new MutableLiveData<>(data);
        when(mockedViewModel.getRouteData()).thenReturn(observed_data);

        scenario.onFragment(new FragmentScenario.FragmentAction<RoutesFragment>() {
            @Override
            public void perform(@NonNull RoutesFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navHostController);
                fragment.routesViewModel = mockedViewModel;
                fragment.bind_views();

                RecyclerView recycler = fragment.getView().findViewById(R.id.recycler_view_routes);
                View item = recycler.getLayoutManager().findViewByPosition(0);
                TextView name = item.findViewById(R.id.item_view_routename);
                assertEquals("Mission Bay", name.getText());

                data.set(0, aardvark_park);
                observed_data.postValue(data);

                item = recycler.getLayoutManager().findViewByPosition(0);
                name = item.findViewById(R.id.item_view_routename);
                assertEquals("Aardvark Park", name.getText());
            }
        });
    }*/
}
