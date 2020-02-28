package com.example.team_project_team6.ui.team;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class TabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
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
            case 1:
                ProposedWalkFragment proposedWalkFragment = new ProposedWalkFragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
