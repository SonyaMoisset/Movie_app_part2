package com.sonyamoisset.android.movieapp.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sonyamoisset.android.movieapp.R;
import com.sonyamoisset.android.movieapp.model.Trailer;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    private final Context context;
    private final List<Trailer> trailerList;

    public TrailersAdapter(Context context, List<Trailer> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        private final TextView movieTrailerName;

        private TrailerViewHolder(View view) {
            super(view);
            movieTrailerName = view.findViewById(R.id.movie_trailer_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        String trailer_id = trailerList.get(position).getKey();

                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(context.getString(R.string.youtube_web_intent) + trailer_id));
                        Intent appIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(context.getString(R.string.youtube_app_intent) + trailer_id));

                        try {
                            appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            appIntent.putExtra(context.getString(R.string.TRAILER_ID_KEY), trailer_id);

                            context.startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            webIntent.putExtra(context.getString(R.string.TRAILER_ID_KEY), trailer_id);

                            context.startActivity(webIntent);
                        }
                    }
                }
            });
        }
    }

    @Override
    public TrailersAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer_list_items, viewGroup, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.TrailerViewHolder holder, int position) {
        String trailerName = trailerList.get(position).getName();

        holder.movieTrailerName.setText(trailerName);
    }

    @Override
    public int getItemCount() {
        return (trailerList == null) ? 0 : trailerList.size();
    }
}
