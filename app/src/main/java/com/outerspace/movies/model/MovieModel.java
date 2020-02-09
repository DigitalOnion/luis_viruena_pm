package com.outerspace.movies.model;

import android.os.AsyncTask;

import com.outerspace.movies.model.api.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieModel {

    public interface MovieCallback {
        void callback(List<Movie> movieListResult);
    }

    public interface UiWaitingCallback {
        void callback(boolean uiWaiting);
    }

    public static final String POSTER_SIZE = "w185";
    public static final String POSTER_SIZE_BIG = "w780";

    private static final String API_KEY = "api_key";
    private static final String LANGUAGE = "language";
    private static final String PAGE = "page";

    private static final String API_KEY_VALUE = "6ede68890852274974d5af2c4941796e";
    private static final String US_ENGLISH = "en-US";

    private static final String API_BASE_URL = "https://api.themoviedb.org/";
    private static final String TOP_RATED_ENDPOINT = "3/movie/top_rated";
    private static final String POPULAR_ENDPOINT = "3/movie/popular";

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/";
    private static final String POSTER_ENDPOINT = "t/p/";


    private static String getApiKey() {
        return API_KEY + "=" + API_KEY_VALUE;
    }

    private static String getLanguage() {
        return LANGUAGE + "=" + US_ENGLISH;
    }

    private static String getPage(int page) {
        return PAGE + "=" + page;
    }


    public static void getPopularMovies(UiWaitingCallback uiWaitingCallback, MovieCallback movieCallback) {
        getMovieList(uiWaitingCallback, movieCallback,
                API_BASE_URL + POPULAR_ENDPOINT + "?"
                        + getApiKey() + "&"
                        + getLanguage() + "&"
                        + getPage(1));
    }

    public static void getTopRatedMovies(UiWaitingCallback uiWaitingCallback, MovieCallback movieCallback) {
        getMovieList(uiWaitingCallback, movieCallback,
                API_BASE_URL + TOP_RATED_ENDPOINT + "?"
                        + getApiKey() + "&"
                        + getLanguage() + "&"
                        + getPage(1));
    }

    private static void getMovieList(final UiWaitingCallback uiWaitingCallback, final MovieCallback movieCallback, String urlString) {
        try {
            new AsyncTask<URL, Void, List<Movie>>() {
                @Override
                protected void onPreExecute() {
                    uiWaitingCallback.callback(true);
                }

                @Override
                protected List<Movie> doInBackground(URL... urls) {
                    return getMovieListResponse(urls[0]);
                }

                @Override
                protected void onPostExecute(List<Movie> movieList) {
                    movieCallback.callback(movieList);
                    uiWaitingCallback.callback(false);
                }
            }.execute(new URL(urlString));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static List<Movie> getMovieListResponse(URL movieUrl) {
        List<Movie> movieList = new ArrayList<>();
        try {
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray jsonArray = json.getJSONArray("results");

            for(int iJson = 0; iJson < jsonArray.length(); iJson++) {
                JSONObject jsonMovie = jsonArray.getJSONObject(iJson);
                JSONArray fieldNames = jsonMovie.names();
                Movie movie = new Movie();
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
