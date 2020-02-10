package com.outerspace.movies;

import android.content.Intent;
import android.view.View;

import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.model.UiWaitingCallback;
import com.outerspace.movies.model.api.Movie;

import java.util.List;

class MainPresenter {
    static final String MOVIE = "movie";
    private MainView mainView;

    MainPresenter(MainView mainView) {
        this.mainView = mainView;
    }

    private UiWaitingCallback uiWaitingCallback = new UiWaitingCallback() {
        @Override
        public void callback(boolean uiWaiting) {
            mainView.getProgressBar().setVisibility(uiWaiting ? View.VISIBLE : View.INVISIBLE);
        }
    };

    void presentMostPopular(final MainGridAdapter adapter) {
        MovieModel.getPopularMovies(uiWaitingCallback, getMovieCallback(adapter));
    }

    void presentTopRated(final MainGridAdapter adapter) {
        MovieModel.getTopRatedMovies(uiWaitingCallback, getMovieCallback(adapter));
    }

    private MovieModel.MovieCallback getMovieCallback(final MainGridAdapter adapter) {
        return new MovieModel.MovieCallback() {
            @Override
            public void callback(List<Movie> movieListResult) {
                adapter.setMovies(movieListResult);
                mainView.getRecycler().setAdapter(adapter);
            }
        };
    }

    void onMovieClickListener(Movie movie) {
        Intent intent = new Intent(mainView.getContext(), MovieDetailActivity.class);
        intent.putExtra(MOVIE, movie);
        mainView.getContext().startActivity(intent);
    }
}
