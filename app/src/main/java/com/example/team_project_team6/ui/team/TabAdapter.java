package com.example.team_project_team6.ui.team;

import android.content.Context;

import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.TeamMember;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class TabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    List<TeamMember> items;

    public TabAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MemberFragment memberFragment = new MemberFragment();
                return memberFragment;
            case 1:
                ProposedWalkFragment proposedWalkFragment = new ProposedWalkFragment();
                return proposedWalkFragment;
            default:
                return null;
        }
    }

    public void updateData(ArrayList<TeamMember> data) {
        this.items = data;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
