package com.example.team_project_team6.ui.team;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Features;
import com.example.team_project_team6.model.ProposedWalk;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.TeamMember;
import com.example.team_project_team6.model.Walk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ScheduledFragmentTest {

    @Test
    public void TestWalkScheduled() {
        final TestNavHostController navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.mobile_navigation);

        MutableLiveData<ProposedWalk> proposedWalkMutableLiveData = new MutableLiveData<>();

        TeamViewModel mockedTeamViewModel = mock(TeamViewModel.class);
        when(mockedTeamViewModel.getProposedWalkData()).thenReturn(proposedWalkMutableLiveData);
        when(mockedTeamViewModel.isMyProposedWalk()).thenReturn(true);
        when(mockedTeamViewModel.getHasProposedWalk()).thenReturn(true);
        when(mockedTeamViewModel.getAllMemberGoingData()).thenReturn(new MutableLiveData<>());

        Bundle bundle = new Bundle();
        FragmentScenario<ProposedWalkFragment> scenario = FragmentScenario.launchInContainer(ProposedWalkFragment.class, bundle, R.style.Theme_AppCompat, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader,
                                        @NonNull String className) {
                ProposedWalkFragment fragment = new ProposedWalkFragment();
                ProposedWalkFragment.teamViewModel = mockedTeamViewModel;

                return fragment;
            }
        });

        ProposedWalk proposedWalk = new ProposedWalk();
        proposedWalk.setpDayMonthYearDate("123-321");
        proposedWalk.setpRoute(new Route(new Walk(), "University of California, San Diego, EBU3B", null, "Test Walk Notes", new Features(), "Mission Bay", "wsh", new TeamMember()));
        proposedWalk.setProposer("example@example.com");
        proposedWalk.setScheduled(true);

        proposedWalkMutableLiveData.postValue(proposedWalk);

        scenario.onFragment(fragment -> {
            Button schedule_button = fragment.getView().findViewById(R.id.bt_Schedule);
            assertEquals(schedule_button.getVisibility(), View.INVISIBLE);

            Button withdraw_button = fragment.getView().findViewById(R.id.bt_withdraw);
            assertEquals(withdraw_button.getVisibility(), View.INVISIBLE);

            Button accept_button = fragment.getView().findViewById(R.id.bt_acceptWalk);
            assertEquals(accept_button.getVisibility(), View.INVISIBLE);

            Button decline_time_button = fragment.getView().findViewById(R.id.bt_declineTime);
            assertEquals(decline_time_button.getVisibility(), View.INVISIBLE);

            Button decline_route_button = fragment.getView().findViewById(R.id.bt_declineRoute);
            assertEquals(decline_route_button.getVisibility(), View.INVISIBLE);
        });
    }

    @Test
    public void TestWalkScheduledNotYours() {
        final TestNavHostController navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.mobile_navigation);

        MutableLiveData<ProposedWalk> proposedWalkMutableLiveData = new MutableLiveData<>();

        TeamViewModel mockedTeamViewModel = mock(TeamViewModel.class);
        when(mockedTeamViewModel.getProposedWalkData()).thenReturn(proposedWalkMutableLiveData);
        when(mockedTeamViewModel.isMyProposedWalk()).thenReturn(false);
        when(mockedTeamViewModel.getHasProposedWalk()).thenReturn(true);
        when(mockedTeamViewModel.getAllMemberGoingData()).thenReturn(new MutableLiveData<>());

        Bundle bundle = new Bundle();
        FragmentScenario<ProposedWalkFragment> scenario = FragmentScenario.launchInContainer(ProposedWalkFragment.class, bundle, R.style.Theme_AppCompat, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader,
                                        @NonNull String className) {
                ProposedWalkFragment fragment = new ProposedWalkFragment();
                ProposedWalkFragment.teamViewModel = mockedTeamViewModel;

                return fragment;
            }
        });

        ProposedWalk proposedWalk = new ProposedWalk();
        proposedWalk.setpDayMonthYearDate("123-321");
        proposedWalk.setpRoute(new Route(new Walk(), "University of California, San Diego, EBU3B", null, "Test Walk Notes", new Features(), "Mission Bay", "wsh", new TeamMember()));
        proposedWalk.setProposer("example@example.com");
        proposedWalk.setScheduled(true);

        proposedWalkMutableLiveData.postValue(proposedWalk);

        scenario.onFragment(fragment -> {
            Button schedule_button = fragment.getView().findViewById(R.id.bt_Schedule);
            assertEquals(schedule_button.getVisibility(), View.INVISIBLE);

            Button withdraw_button = fragment.getView().findViewById(R.id.bt_withdraw);
            assertEquals(withdraw_button.getVisibility(), View.INVISIBLE);

            Button accept_button = fragment.getView().findViewById(R.id.bt_acceptWalk);
            assertEquals(accept_button.getVisibility(), View.VISIBLE);

            Button decline_time_button = fragment.getView().findViewById(R.id.bt_declineTime);
            assertEquals(decline_time_button.getVisibility(), View.VISIBLE);

            Button decline_route_button = fragment.getView().findViewById(R.id.bt_declineRoute);
            assertEquals(decline_route_button.getVisibility(), View.VISIBLE);
        });
    }
}