package com.outerspace.movies.model;

import android.os.AsyncTask;

import com.outerspace.movies.MediaPresenter;
import com.outerspace.movies.model.api.Movie;
import com.outerspace.movies.model.api.MovieDetail;
import com.outerspace.movies.model.api.Review;
import com.outerspace.movies.model.api.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailModel extends BaseMovieModel {
    public interface MovieDetailCallback {
        void callback(MovieDetail detail);
    }

    public static void fetchMovieDetail(final Movie movie, final MovieDetailCallback movieDetailCallback) {
        final String urlString = getDetailEndpoint(movie.id);
        new AsyncTask<String, Void, MovieDetail>() {

            Exception taskException = null;

            @Override
            protected MovieDetail doInBackground(String... urlStrings) {

                try {
                    return getMovieDetailFromURL(new URL(urlStrings[0]));
                } catch (JSONException | IOException  e) {
                    taskException = e;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(MovieDetail movieDetail) {
                if(taskException == null) {
                    movieDetail.favorite = movie.favorite;
                    movieDetailCallback.callback(movieDetail);
                } else {
                    taskException.printStackTrace();
                    // todo handle exception
                }
            }
        }.execute(urlString);
    }

    private static MovieDetail getMovieDetailFromURL(URL url) throws JSONException, IOException {
        String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
        JSONObject json = new JSONObject(jsonResponse);
        JSONArray fieldNames = json.names();
        MovieDetail detail = new MovieDetail();
        for (int iField = 0; iField < fieldNames.length(); iField++) {
            String name = fieldNames.get(iField).toString();
            switch (name) {
                case "adult":
                    detail.adult = json.getBoolean(name);
                    break;
                case "backdrop_path":
                    detail.backdropPath = json.getString(name);
                    break;
                case "belongs_to_collection":
                    // detail.belongsToCollection = json.getObject(name);
                    break;
                case "budget":
                    detail.budget = json.getInt(name);
                    break;
                case "genres":
                    // detail.genres = json.getList < Genre > (name);
                    break;
                case "homepage":
                    detail.homepage = json.getString(name);
                    break;
                case "id":
                    detail.id = json.getInt(name);
                    break;
                case "imdb_id":
                    detail.imdbId = json.getString(name);
                    break;
                case "original_language":
                    detail.originalLanguage = json.getString(name);
                    break;
                case "original_title":
                    detail.originalTitle = json.getString(name);
                    break;
                case "overview":
                    detail.overview = json.getString(name);
                    break;
                case "popularity":
                    detail.popularity = json.getDouble(name);
                    break;
                case "poster_path":
                    detail.posterPath = json.getString(name);
                    break;
                case "production_companies":
                    // detail.productionCompanies = json.getList < ProductionCompany > (name);
                    break;
                case "production_countries":
                    // detail.productionCountries = json.getList < ProductionCountry > (name);
                    break;
                case "release_date":
                    detail.releaseDate = json.getString(name);
                    break;
                case "revenue":
                    detail.revenue = json.getInt(name);
                    break;
                case "runtime":
                    detail.runtime = json.getInt(name);
                    break;
                case "spoken_languages":
                    // detail.spokenLanguages = json.getList < SpokenLanguage > (name);
                    break;
                case "status":
                    detail.status = json.getString(name);
                    break;
                case "tagline":
                    detail.tagline = json.getString(name);
                    break;
                case "title":
                    detail.title = json.getString(name);
                    break;
                case "video":
                    detail.video = json.getBoolean(name);
                    break;
                case "vote_average":
                    detail.voteAverage = json.getInt(name);
                    break;
            }
        }
        return detail;
    }

    public static void fetchTrailers(int movieId, final MediaPresenter.MediaCallback<Trailer> callback) {
        final String urlString = getTrailersEndpoint(movieId);
            new AsyncTask<String, Void, List<Trailer>>() {

                Exception taskException = null;

                @Override
                protected List<Trailer> doInBackground(String... urlStrings) {
                    try {
                        return getTrailerListFromURL(new URL(urlStrings[0]));
                    } catch (IOException | JSONException e) {
                        taskException = e;
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(List<Trailer> trailers) {
                    if(taskException == null) {
                        callback.call(trailers);
                    } else {
                        taskException.printStackTrace();
                        // todo handle exception
                    }
                }
            }.execute(urlString);
    }

    private static List<Trailer> getTrailerListFromURL(URL url) throws IOException, JSONException {
        String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
        JSONObject jsonObjResponse = new JSONObject(jsonResponse);
        JSONArray jsonVideos = jsonObjResponse.getJSONArray("results");

        List<Trailer> trailerList = new ArrayList<>();
        for(int i = 0; i < jsonVideos.length(); i++) {
            JSONObject json = (JSONObject) jsonVideos.get(i);
            Trailer trailer = new Trailer();
            JSONArray names = json.names();
            for(int iName = 0; iName < names.length(); iName++) {
                String fieldName = names.get(iName).toString();
                switch (fieldName) {
                    case "id":
                        trailer.id = json.getString(fieldName);
                        break;
                    case "iso_639_1":
                        trailer.iso6391 = json.getString(fieldName);
                        break;
                    case "iso_3166_1":
                        trailer.iso31661 = json.getString(fieldName);
                        break;
                    case "key":
                        trailer.key = json.getString(fieldName);
                        break;
                    case "name":
                        trailer.name = json.getString(fieldName);
                        break;
                    case "site":
                        trailer.site = json.getString(fieldName);
                        break;
                    case "size":
                        trailer.size = json.getInt(fieldName);
                        break;
                    case "type":
                        trailer.type = json.getString(fieldName);
                        break;
                }
            }
            if(trailer.site.equalsIgnoreCase( "YouTube") && trailer.type.equalsIgnoreCase("Trailer")) {
                trailerList.add(trailer);
            }
        }
        return trailerList;
    }

    public static void fetchReviews(int movieId, final MediaPresenter.MediaCallback<Review> callback) {
        final String urlString = getReviewEndpoint(movieId);
        new AsyncTask<String, Void, List<Review>>() {

            Exception taskException = null;

            @Override
            protected List<Review> doInBackground(String... urlStrings) {
                try {
                    return getReviewListFromURL(new URL(urlStrings[0]));
                } catch (IOException | JSONException e) {
                    taskException = e;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Review> reviews) {
                if(taskException == null) {
                    callback.call(reviews);
                } else {
                    taskException.printStackTrace();
                    // todo handle exception
                }
            }
        }.execute(urlString);
    }

    private static List<Review> getReviewListFromURL(URL url) throws IOException, JSONException {
        String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
        JSONObject jsonObjResponse = new JSONObject(jsonResponse);
        JSONArray jsonReviews = jsonObjResponse.getJSONArray("results");

        List<Review> reviewList = new ArrayList<>();
        for(int i = 0; i < jsonReviews.length(); i++) {
            JSONObject json = (JSONObject) jsonReviews.get(i);
            Review review = new Review();
            JSONArray names = json.names();
            for(int iName = 0; iName < names.length(); iName++) {
                String fieldName = names.get(iName).toString();
                switch (fieldName) {
                    case "id":
                        review.id = json.getString(fieldName);
                        break;
                    case "author":
                        review.author = json.getString(fieldName);
                        break;
                    case "content":
                        review.content = json.getString(fieldName);
                        break;
                    case "url":
                        review.url = json.getString(fieldName);
                        break;
                }
            }
            reviewList.add(review);
        }
        return reviewList;
    }
}
