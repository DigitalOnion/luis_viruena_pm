package com.outerspace.movies;

import com.outerspace.movies.model.MovieDetailModel;
import com.outerspace.movies.model.api.Trailer;
import com.outerspace.movies.model.persistence.MovieRepository;

import java.util.List;

public class TrailerPresenter {
    private TrailerView trailerView;


    public interface TrailerCallback {
        void call(List<Trailer> resultTrailerList);
    }

    public TrailerPresenter(TrailerView trailerView) {
        this.trailerView = trailerView;
    }

    public void fetchTrailers(int movieId, TrailerCallback callback) {
        MovieDetailModel.fetchTrailers(movieId, callback);
    }

    void OnTrailerClickListener(Trailer trailer) {
        trailerView.showTrailer(trailer);
    }
}
