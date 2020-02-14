package com.example.team_project_team6.ui.routes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project_team6.R;
import com.example.team_project_team6.model.Route;
import com.example.team_project_team6.model.Walk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RouteViewAdapter extends RecyclerView.Adapter<RouteViewAdapter.RouteViewHolder> {
    private ArrayList<Route> items;
    private static ClickListener favoriteClickListener;
    private static ClickListener itemClickListener;

    static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView trailName, steps, distance, lastCompleted;
        ImageButton favoriteButton;

        RouteViewHolder(final View itemView) {
            super(itemView);

            trailName = itemView.findViewById(R.id.item_view_routename);
            steps = itemView.findViewById(R.id.item_view_steps);
            distance = itemView.findViewById(R.id.item_view_dist);
            lastCompleted = itemView.findViewById(R.id.item_view_date);
            favoriteButton = itemView.findViewById(R.id.item_view_favorite);

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteClickListener.onItemClick(getAdapterPosition(), v);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    void setOnFavoriteClickListener(ClickListener listener) {
        RouteViewAdapter.favoriteClickListener = listener;
    }

    void setOnItemClickListener(ClickListener listener) {
        RouteViewAdapter.itemClickListener = listener;
    }

    public RouteViewAdapter() {
        this.items = new ArrayList<>();
    }

    public RouteViewAdapter(ArrayList<Route> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_itemview, parent, false);
        return new RouteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        if (items.get(position).getFeatures().isFavorite() && items.get(position).getFeatures() != null) {
            holder.favoriteButton.setBackgroundResource(R.drawable.ic_filled_star);
        } else {
            holder.favoriteButton.setBackgroundResource(R.drawable.ic_empty_star);
        }


        holder.trailName.setText(items.get(position).getName());

        Route currRoute = items.get(position);
        Walk walkInCurrRoute = items.get(position).getWalk();

        Log.i("RouteViewAdapter onBindViewHolder", "route name: " + currRoute.getName());
        if(currRoute == null) {
            Log.e("RouteViewAdapter onBindViewHolder","currRoute is null");
        } else {
            Log.e("RouteViewAdapter onBindViewHolder","walkInCurrRoute is null");
        }

        String steps = String.format(Locale.ENGLISH, "%d steps", walkInCurrRoute.getStep());
        holder.steps.setText(steps);

        String dist = String.format(Locale.ENGLISH, "%.2f mi", walkInCurrRoute.getDist());
        holder.distance.setText(dist);

        if (items.get(position).getLastStartDate() != null) {
            Date dateLastWalked = items.get(position).getLastStartDate().getTime();
            SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
            holder.lastCompleted.setText(format.format(dateLastWalked));
        } else {
            holder.lastCompleted.setText(R.string.never_completed);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(ArrayList<Route> data) {
        this.items = data;
    }
}