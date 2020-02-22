package com.outerspace.movies.view;

import com.outerspace.movies.model.MovieDetailModel;
import com.outerspace.movies.api.Review;
import com.outerspace.movies.api.Trailer;
import com.outerspace.movies.view.TrailerView;

import java.util.List;

public class MediaPresenter {
    private TrailerView trailerView;

    public interface MediaCallback<T> {
        void call(List<T> resultMediaList);
    }

    public MediaPresenter(TrailerView trailerView) {
        this.trailerView = trailerView;
    }

    public void fetchTrailers(int movieId, MediaCallback<Trailer> callback) {
        MovieDetailModel.fetchTrailers(movieId, callback);
    }

    public void fetchReviews(int movieId, MediaCallback<Review> callback) {
        MovieDetailModel.fetchReviews(movieId, callback);
    }
    public void OnTrailerClickListener(Trailer trailer) {
        trailerView.showTrailer(trailer);
    }
}
