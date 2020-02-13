package com.outerspace.movies.model;

import android.os.AsyncTask;

import com.outerspace.movies.model.api.Movie;
import com.outerspace.movies.model.persistence.MovieRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieModel extends BaseMovieModel {

    public interface MovieCallback {
        void callback(List<Movie> movieListResult);
    }

    public static void getPopularMovies(MovieCallback movieCallback) {
        getMovieList(movieCallback,
                API_BASE_URL + POPULAR_ENDPOINT + "?"
                        + getApiKey() + "&"
                        + getLanguage() + "&"
                        + getPage(1));
    }

    public static void getTopRatedMovies(MovieCallback movieCallback) {
        getMovieList(movieCallback,
                API_BASE_URL + TOP_RATED_ENDPOINT + "?"
                        + getApiKey() + "&"
                        + getLanguage() + "&"
                        + getPage(1));
    }

    private static void getMovieList(final MovieCallback movieCallback, String urlString) {
        try {
            new AsyncTask<URL, Void, List<Movie>>() {

                @Override
                protected List<Movie> doInBackground(URL... urls) {
                    return getMovieListFromURL(urls[0]);
                }

                @Override
                protected void onPostExecute(List<Movie> movieList) {
                    movieCallback.callback(movieList);
                }
            }.execute(new URL(urlString));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static List<Movie> getMovieListFromURL(URL movieUrl) {
        final List<Movie> movieList = new ArrayList<>();
        try {
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray jsonArray = json.getJSONArray("results");

            for (int iJson = 0; iJson < jsonArray.length(); iJson++) {
                JSONObject jsonMovie = jsonArray.getJSONObject(iJson);
                JSONArray fieldNames = jsonMovie.names();
                final Movie movie = new Movie();
                for (int iField = 0; iField < fieldNames.length(); iField++) {
                    String name = jsonMovie.names().get(iField).toString();
                    switch (name) {
                        case "popularity":
                            movie.popularity = jsonMovie.getDouble(name);
                            break;
                        case "vote_count":
                            movie.voteCount = jsonMovie.getInt(name);
                            break;
                        case "video":
                            movie.video = jsonMovie.getBoolean(name);
                            break;
                        case "poster_path":
                            movie.posterPath = jsonMovie.getString(name);
                            break;
                        case "id":
                            movie.id = jsonMovie.getInt(name);
                            break;
                        case "adult":
                            movie.adult = jsonMovie.getBoolean(name);
                            break;
                        case "backdrop_path":
                            movie.backdropPath = jsonMovie.getString(name);
                            break;
                        case "original_language":
                            movie.originalLanguage = jsonMovie.getString(name);
                            break;
                        case "original_title":
                            movie.originalTitle = jsonMovie.getString(name);
                            break;
                        case "title":
                            movie.title = jsonMovie.getString(name);
                            break;
                        case "vote_average":
                            movie.voteAverage = jsonMovie.getInt(name);
                            break;
                        case "overview":
                            movie.overview = jsonMovie.getString(name);
                            break;
                        case "release_date":
                            movie.releaseDate = jsonMovie.getString(name);
                            break;
                    }
                }
                movieList.add(movie);
            }

            for (final Movie movie : movieList) {
                MovieRepository.getInstance().isFavoriteAsync(movie.id, new MovieRepository.MovieRepositoryCallback<Boolean>() {
                    @Override
                    public void call(Boolean favorite) {
                        movie.favorite = favorite;
                    }
                });
            }

            return movieList;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return movieList;
    }

    public static String getPosterPathURL(String posterPath) {
        return POSTER_BASE_URL + POSTER_ENDPOINT + POSTER_SIZE + posterPath;
    }

    public static String getPosterPathURL(String posterSize, String posterPath) {
        return POSTER_BASE_URL + POSTER_ENDPOINT + posterSize + posterPath;
    }
}
