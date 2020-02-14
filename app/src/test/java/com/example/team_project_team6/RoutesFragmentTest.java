package com.example.team_project_team6;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
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

        FragmentScenario<RoutesFragment> scenario = FragmentScenario.launchInContainer(RoutesFragment.class);
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
}
