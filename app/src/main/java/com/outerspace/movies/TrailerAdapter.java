package com.outerspace.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.outerspace.movies.model.api.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {
    private TrailerPresenter presenter;
    private List<Trailer> trailerList;

    public TrailerAdapter(TrailerPresenter presenter) {
        this.presenter = presenter;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = trailerList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrailerHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.movie_trailer_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {
        if (trailerList != null) {
            holder.trailer = trailerList.get(position);
            holder.trailerTitle.setText(holder.trailer.name);
        }
    }

    @Override
    public int getItemCount() {
        if (trailerList != null) {
            return trailerList.size();
        } else {
            return 0;
        }
    }

    public class TrailerHolder extends RecyclerView.ViewHolder {
        Trailer trailer;
        TextView trailerTitle;
        ConstraintLayout trailerLayout;

        public TrailerHolder(@NonNull View itemView) {
            super(itemView);
            trailerTitle = itemView.findViewById(R.id.trailer_title);
            trailerLayout = itemView.findViewById(R.id.trailer_item_layout);
            trailerLayout.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            presenter.OnTrailerClickListener(trailer);
                        }
                    });
        }
    }
}
