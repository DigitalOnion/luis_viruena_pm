package com.outerspace.movies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.model.UiWaitingCallback;
import com.outerspace.movies.model.api.Movie;
import com.outerspace.movies.model.persistence.MovieRepository;

import java.util.List;

class MainPresenter {
    static final String MOVIE = "movie";
    private MainView mainView;

    MainPresenter(MainView mainView) {
        this.mainView = mainView;
    }

    void presentMostPopular(final MainGridAdapter adapter) {
        activateProgressBar(true);
        MovieModel.getPopularMovies(getMovieCallback(adapter));
    }

    void presentTopRated(final MainGridAdapter adapter) {
        activateProgressBar(true);
        MovieModel.getTopRatedMovies(getMovieCallback(adapter));
    }

    void presentFavorites(final MainGridAdapter adapter) {
        activateProgressBar(true);
        MovieRepository.getFavoriteMovies(getMovieCallback(adapter));
    }

    void clearFavorites(Context context) {
        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MovieRepository.clearAllMoviesAsync();
            }
        };
        new AlertDialog.Builder(context)
                .setTitle(R.string.clear_favorites_title)
                .setMessage(R.string.clear_favorites_message)
                .setPositiveButton(R.string.clear_favorites_clear, positiveListener)
                .setNegativeButton(R.string.clear_favorites_cancel, null)
                .create()
                .show();
    }

    private MovieModel.MovieCallback getMovieCallback(final MainGridAdapter adapter) {
        return new MovieModel.MovieCallback() {
            @Override
            public void callback(List<Movie> movieListResult) {
                adapter.setMovies(movieListResult);
                mainView.getRecycler().setAdapter(adapter);
                activateProgressBar(false);
            }
        };
    }

    private void activateProgressBar(boolean active) {
            mainView.getProgressBar().setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    void onMovieClickListener(Movie movie) {
        Intent intent = new Intent(mainView.getContext(), MovieDetailActivity.class);
        intent.putExtra(MOVIE, movie);
        mainView.getContext().startActivity(intent);
    }
}
