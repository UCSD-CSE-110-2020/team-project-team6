package com.example.team_project_team6.ui.team;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.TeamMember;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MemberFragmentTest {

    @Test
    public void TestNoSavedTeamList() {
        ArrayList<TeamMember> teamMemberData = new ArrayList<>();
        HashMap<String, String> teamInviterData = new HashMap<>();

        final TestNavHostController navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.mobile_navigation);

        Bundle bundle = new Bundle();
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<MemberFragment> scenario = FragmentScenario.launchInContainer(MemberFragment.class, bundle, R.style.Theme_AppCompat, factory);
        final TeamViewModel mockedViewModel = mock(TeamViewModel.class);
        when(mockedViewModel.getTeamMemberData()).thenReturn(new MutableLiveData<>(teamMemberData));
        when(mockedViewModel.getTeamInviterData()).thenReturn(new MutableLiveData<>(teamInviterData));

        scenario.onFragment(new FragmentScenario.FragmentAction<MemberFragment>() {
            @Override
            public void perform(@NonNull MemberFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navHostController);
                fragment.teamViewModel = mockedViewModel;

                fragment.bind_views();

                ListView listView = fragment.getView().findViewById(R.id.list_team_members);
                assertEquals(0, listView.getChildCount());
            }
        });
    }

    @Test
    public void TestMultipleTeamMemberInList() {
        ArrayList<TeamMember> data = new ArrayList<>();
        HashMap<String, String> teamInviterData = new HashMap<>();
        data.add(new TeamMember("Sarah", "Soap", "sarah.soap@gmail.com"));
        data.add(new TeamMember("Ellen", "Elephant", "ellen.elephant@gmail.com"));
        data.add(new TeamMember("Bob", "Builder", "bob.builder@gmail.com"));

        final TestNavHostController navHostController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.mobile_navigation);

        Bundle bundle = new Bundle();
        FragmentFactory factory = new FragmentFactory();
        FragmentScenario<MemberFragment> scenario = FragmentScenario.launchInContainer(MemberFragment.class, bundle, R.style.Theme_AppCompat, factory);
        final TeamViewModel mockedViewModel = mock(TeamViewModel.class);
        when(mockedViewModel.getTeamMemberData()).thenReturn(new MutableLiveData<>(data));
        when(mockedViewModel.getTeamInviterData()).thenReturn(new MutableLiveData<>(teamInviterData));

        scenario.onFragment(new FragmentScenario.FragmentAction<MemberFragment>() {
            @Override
            public void perform(@NonNull MemberFragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), navHostController);
                fragment.teamViewModel = mockedViewModel;

                fragment.bind_views();

                ListView listViewTeamMember = fragment.getView().findViewById(R.id.list_team_members);
                ListView listViewTeamInviter = fragment.getView().findViewById(R.id.list_invited_team_members);
                assertEquals(0, listViewTeamInviter.getChildCount());
                assertEquals(3, data.size());
            }
        });
    }
}