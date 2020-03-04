package com.example.team_project_team6.ui.team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.TeamMember;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class TeamArrayAdapter extends ArrayAdapter<TeamMember> {
    private ArrayList<TeamMember> items;
    public View v;
    int resource;

    public TeamArrayAdapter(Context context, int resource, LiveData<ArrayList<TeamMember>> items) {
        super(context, resource, items.getValue());
        this.items = items.getValue();
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TeamMember teamMember = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        TextView teamMemberName = (TextView) convertView.findViewById(R.id.txt_team_member_name);
        teamMemberName.setText(teamMember.getFirstName() + " " + teamMember.getLastName());
        return convertView;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    public void updateData(ArrayList<TeamMember> data) {
        this.items = data;
    }
}
