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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RouteViewAdapter extends FirestoreRecyclerAdapter<Route, RouteViewAdapter.RouteViewHolder> {
    private ArrayList<Route> items;
    private static ClickListener favoriteClickListener;
    private static ClickListener itemClickListener;

    class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView trailName, steps, distance, lastCompleted, textFeatures;
        ImageButton favoriteButton;

        RouteViewHolder(final View itemView) {
            super(itemView);

            trailName = itemView.findViewById(R.id.item_view_routename);
            System.out.println(trailName);
            steps = itemView.findViewById(R.id.item_view_steps);
            distance = itemView.findViewById(R.id.item_view_dist);
            lastCompleted = itemView.findViewById(R.id.item_view_date);
            favoriteButton = itemView.findViewById(R.id.item_view_favorite);
            textFeatures = itemView.findViewById(R.id.item_view_features);

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && favoriteClickListener != null) {
                        favoriteClickListener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                        itemClickListener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface ClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    void setOnFavoriteClickListener(ClickListener listener) {
        RouteViewAdapter.favoriteClickListener = listener;
    }

    void setOnItemClickListener(ClickListener listener) {
        RouteViewAdapter.itemClickListener = listener;
    }

    public RouteViewAdapter(@NonNull FirestoreRecyclerOptions<Route> options) {
        super(options);
        this.items = new ArrayList<>();
    }

//    public RouteViewAdapter(ArrayList<Route> items) {
//        this.items = items;
//    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_itemview, parent, false);
        return new RouteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position, @NonNull Route model) {

        if (model.getFeatures().isFavorite() && model.getFeatures() != null) {
            holder.favoriteButton.setBackgroundResource(R.drawable.ic_filled_star);
        } else {
            holder.favoriteButton.setBackgroundResource(R.drawable.ic_empty_star);
        }

        String features = getFeaturesString(model);
        holder.textFeatures.setText(features);

        holder.trailName.setText(model.getName());

        Route currRoute = model;
        Walk walkInCurrRoute = model.getWalk();

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

        if (model.getLastStartDate() != null) {
            Date dateLastWalked = model.getLastStartDate().getTime();
            SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
            holder.lastCompleted.setText(format.format(dateLastWalked));
        } else {
            holder.lastCompleted.setText(R.string.never_completed);
        }
    }

//    @Override
//    public int getItemCount() {
//        return items.size();
//    }

    public void updateData(ArrayList<Route> data) {
        this.items = data;
    }

    private String getFeaturesString(Route route) {
        String features = "";

        // difficulty
        switch (route.getFeatures().getLevel()) {
            case 1:
                features += "\uD83C\uDDEA ";
                break;
            case 2:
                features += "\uD83C\uDDF2 ";
                break;
            case 3:
                features += "\uD83C\uDDED ";
                break;
            default:
        }

        // one way or circular
        switch(route.getFeatures().getDirectionType()) {
            case 1:
                features += "\u2197\ufe0f ";
                break;
            case 2:
                features += "\uD83D\uDD04 ";
                break;
            default:
        }

        // flat/hilly
        switch (route.getFeatures().getTerrain()) {
            case 1:
                features += "\uD83C\uDDEB ";
                break;
            case 2:
                features += "\u26f0\ufe0f ";
                break;
            default:
        }

        // street/trail
        switch (route.getFeatures().getType()) {
            case 1:
                features += "\u2796 ";
                break;
            case 2:
                features += "\uD83C\uDF32 ";
                break;
            default:
        }

        // even/not even
        switch (route.getFeatures().getSurface()) {
            case 1:
                features += "\u2696\ufe0f ";
                break;
            case 2:
                features += "\u3030\ufe0f ";
                break;
            default:
        }

        // remove trailing space
        if (!features.isEmpty()) {
            features = features.substring(0, features.length() - 1);
        }

        return features;
    }
}