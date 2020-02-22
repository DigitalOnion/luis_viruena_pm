package com.outerspace.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.outerspace.movies.api.BaseMedia;
import com.outerspace.movies.api.Review;
import com.outerspace.movies.api.Trailer;
import com.outerspace.movies.view.MediaPresenter;

import java.util.ArrayList;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MEDIA_TRAILER = 10;
    private static final int MEDIA_REVIEW = 20;

    private MediaPresenter presenter;
    private List<BaseMedia> mediaList = new ArrayList<>();

    public MediaAdapter(MediaPresenter presenter) {
        this.presenter = presenter;
    }

    public void addAllMedia(List<BaseMedia> mediaList) {
        this.mediaList.addAll(mediaList);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MEDIA_TRAILER)
                return new TrailerHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.movie_trailer_item, parent, false));
        else
                return new ReviewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.movie_review_item, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (mediaList.get(position) instanceof Trailer)
            return MEDIA_TRAILER;
        else
            return MEDIA_REVIEW;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case MEDIA_TRAILER:
                bindTrailer((TrailerHolder) holder, (Trailer) mediaList.get(position));
                return;
            case MEDIA_REVIEW:
                bindReview((ReviewHolder) holder, (Review) mediaList.get(position));
                return;
        }
    }

    private void bindTrailer(TrailerHolder holder, Trailer trailer) {
        holder.trailer = trailer;
        holder.trailerTitle.setText(holder.trailer.name);
    }

    private void bindReview(ReviewHolder holder, Review review) {
        holder.author.setText(review.author);
        holder.content.setText(review.content);
    }

    @Override
    public int getItemCount() {
        if (mediaList != null) {
            return mediaList.size();
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

    public class ReviewHolder extends RecyclerView.ViewHolder {
        Review review;
        TextView author;
        TextView content;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.review_author);
            content = itemView.findViewById(R.id.review_content);
        }
    }
}
