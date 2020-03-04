package com.example.team_project_team6.ui.team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.team_project_team6.R;

import java.util.ArrayList;

public class TeamArrayAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> items;
    public View v;

    public TeamArrayAdapter(Context context, ArrayList<String> items) {
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
        String teamMemberName = (String) getItem(position);

        TextView teamMemberNameView = (TextView) convertView.findViewById(R.id.txt_team_member_name);
        teamMemberNameView.setText(teamMemberName);

        return convertView;
    }

    public void updateData(ArrayList<String> data) {
        this.items = data;
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
