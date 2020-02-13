package com.outerspace.movies;

import com.outerspace.movies.model.MovieDetailModel;
import com.outerspace.movies.model.api.Review;
import com.outerspace.movies.model.api.Trailer;

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
    void OnTrailerClickListener(Trailer trailer) {
        trailerView.showTrailer(trailer);
    }
}
