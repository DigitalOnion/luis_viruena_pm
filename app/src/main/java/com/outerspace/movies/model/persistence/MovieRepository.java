package com.outerspace.movies.model.persistence;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.outerspace.movies.model.api.Movie;

public class MovieRepository {
    private static final String MOVIE_DB_NAME = "movieDatabase";
    private static MovieDatabase movieDatabase = null;

    private static MovieRepository instance = new MovieRepository();       // singleton only instance

    private MovieRepository() { }       // singleton private constructor

    public static void initialize(Context context) {
        movieDatabase = Room.databaseBuilder(context, MovieDatabase.class, MOVIE_DB_NAME).build();
    }

    public static MovieRepository getInstance() {
        return instance;
    }

    public interface MovieRepositoryCallback<T> {
        void call(T result);
    }

    private static class IsFavoriteTask extends AsyncTask<Integer, Void, Boolean> {
        MovieRepositoryCallback<Boolean> callback;

        IsFavoriteTask(MovieRepositoryCallback<Boolean> callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            return MovieRepository.getInstance().isFavorite(integers[0]);
        }

        @Override
        protected void onPostExecute(Boolean isFavorite) {
            callback.call(isFavorite);
        }
    }

    public void isFavoriteAsync(int movieId, MovieRepositoryCallback<Boolean> callback) {
        IsFavoriteTask task = new IsFavoriteTask(callback);
        task.execute(movieId);
    }

    public boolean isFavorite(int movieId) {
        return movieDatabase.movieDao().isFavoriteMovie(movieId);
    }

    private static class FlipFavoriteTask extends AsyncTask<Integer, Void, Boolean> {
        MovieRepositoryCallback<Boolean> callback;

        public FlipFavoriteTask(MovieRepositoryCallback<Boolean> callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            return MovieRepository.getInstance().flipFavorite(integers[0]);
        }

        @Override
        protected void onPostExecute(Boolean favorite) {
            callback.call(favorite);
        }
    }

    public void flipFavoriteAsync(int movieId, MovieRepositoryCallback<Boolean> callback) {
        (new FlipFavoriteTask(callback)).execute(movieId);
    }

    public boolean flipFavorite(int movieId) {
        boolean favorite = !movieDatabase.movieDao().isFavoriteMovie(movieId);
        movieDatabase.movieDao().updateFavorite(movieId, favorite);
        return favorite;
    }

    public boolean isMovieStored(int movieId) {
         return movieDatabase.movieDao().isMovieInDB(movieId);
    }

    public Movie getMovieFromId(int movieId) {
        return movieDatabase.movieDao().getMovieFromId(movieId);
    }

    public void insert(Movie movie) {
        movieDatabase.movieDao().insert(movie);
    };
}
