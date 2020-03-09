package com.example.team_project_team6.ui.team;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.TeamMember;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class TeamArrayAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> items;
    private boolean isViewInvited;

    public TeamArrayAdapter(Context context, ArrayList<String> items, boolean isViewInvited) {
        Log.i(TAG, "Creating TeamArrayAdapter");
        this.context = context;
        this.items = items;
        this.isViewInvited = isViewInvited;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.single_item_list_view, parent, false);
        }

        // Get the data item for this position
        String teamMemberName = (String) getItem(position);

        TextView teamMemberNameView = (TextView) convertView.findViewById(R.id.txt_team_member_name);
        teamMemberNameView.setText(teamMemberName);
        if(isViewInvited) {
            teamMemberNameView.setTypeface(teamMemberNameView.getTypeface(), Typeface.ITALIC);
            teamMemberNameView.setTextColor(Color.GRAY);
        }

        return convertView;
    }

    public void addData(String data) {
        this.items.add(data);
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
