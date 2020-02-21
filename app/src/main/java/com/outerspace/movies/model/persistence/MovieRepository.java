package com.outerspace.movies.model.persistence;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.model.api.Movie;

import java.util.List;

public class MovieRepository {
    private static final String MOVIE_DB_NAME = "movieDatabase";
    protected static MovieDatabase movieDatabase = null;

    protected static MovieRepository instance = new MovieRepository();       // singleton only instance

    protected MovieRepository() {
    }       // singleton private constructor

    public static void initialize(Context context) {
        movieDatabase = Room.databaseBuilder(context, MovieDatabase.class, MOVIE_DB_NAME).build();
    }

    public static MovieRepository getInstance() {
        return instance;
    }

    public interface MovieRepositoryCallback<T> {
        void call(T result);
    }

    private static class SelectFavoritesTask extends AsyncTask<Void, Void, List<Movie>> {
        MovieModel.MovieCallback movieCallback;

        public SelectFavoritesTask(MovieModel.MovieCallback movieCallback) {
            this.movieCallback = movieCallback;
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return movieDatabase.movieDao().selectFavorites();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            movieCallback.callback(movies);
        }
    }

    public static void getFavoriteMovies(MovieModel.MovieCallback movieCallback) {
        SelectFavoritesTask task = new SelectFavoritesTask(movieCallback);
        task.execute();
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

    private static class FlipFavoriteTask extends AsyncTask<Movie, Void, Boolean> {
        MovieRepositoryCallback<Boolean> callback;

        public FlipFavoriteTask(MovieRepositoryCallback<Boolean> callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Movie... movies) {
            Movie movie = movies[0];
            if(!movieDatabase.movieDao().isMovieInDB(movie.id)) {
                movieDatabase.movieDao().insert(movie);
            }
            movie.favorite = !movie.favorite;
            movieDatabase.movieDao().updateFavorite(movie.id, movie.favorite);
            return movie.favorite;
        }

        @Override
        protected void onPostExecute(Boolean favorite) {
            callback.call(favorite);
        }
    }

    public void flipFavoriteAsync(Movie movie, MovieRepositoryCallback<Boolean> callback) {
        (new FlipFavoriteTask(callback)).execute(movie);
    }

    public boolean isMovieStored(int movieId) {
        return movieDatabase.movieDao().isMovieInDB(movieId);
    }

    public Movie getMovieFromId(int movieId) {
        return movieDatabase.movieDao().getMovieFromId(movieId);
    }

    public void insert(Movie movie) {
        movieDatabase.movieDao().insert(movie);
    }

    ;

    public void delete(Movie movie) {
        movieDatabase.movieDao().delete(movie);
    }

    private static class ClearAllMoviesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            movieDatabase.movieDao().clearAllMovies();
            return null;
        }
    }

    public static void clearAllMoviesAsync() {
        new ClearAllMoviesTask().execute();
    }

}
