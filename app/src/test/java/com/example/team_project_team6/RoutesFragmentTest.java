package com.example.team_project_team6;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;

import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.Walk;
import com.example.team_project_team6.ui.routes.RoutesFragment;
import com.example.team_project_team6.ui.routes.RoutesViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class RoutesFragmentTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Test
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
        Route route = new Route(w, "University of California, San Diego, EBU3B", null, "Test Walk Notes", f, "Mission Bay", "wsh", owner);
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
        Route mission = new Route(new Walk(), "", null, "", new Features(), "Mission Bay", "wsh", owner);
        Route aardvark_park = new Route(new Walk(), "", null, "", new Features(), "Aardvark Park", "wsh", owner);
        ArrayList<Route> data = new ArrayList<Route>();
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
        final Route mission = new Route(new Walk(), "", null, "", new Features(), "Mission Bay", "wsh", owner);
        final Route aardvark_park = new Route(new Walk(), "", null, "", new Features(), "Aardvark Park", "wsh", owner);
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
    }
}
