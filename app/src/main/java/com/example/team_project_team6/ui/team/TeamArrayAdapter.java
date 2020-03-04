package com.example.team_project_team6.ui.team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.TeamMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.LiveData;

import static androidx.test.InstrumentationRegistry.getContext;

public class TeamArrayAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TeamMember> items;
    public View v;

    public TeamArrayAdapter(Context context, ArrayList<TeamMember> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.single_item_list_view, parent, false);
        }

        // Get the data item for this position
        TeamMember teamMember = (TeamMember) getItem(position);

        TextView teamMemberName = (TextView) convertView.findViewById(R.id.txt_team_member_name);
        teamMemberName.setText(teamMember.getFirstName() + " " + teamMember.getLastName());

        return convertView;
    }

    public void updateData(ArrayList<TeamMember> data) {
        this.items = data;
    }


    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return (TeamMember) items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
