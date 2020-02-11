package com.outerspace.movies.model;

import android.os.AsyncTask;

import com.outerspace.movies.model.api.Movie;
import com.outerspace.movies.model.api.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieDetailModel extends BaseMovieModel {
    public interface MovieDetailCallback {
        void callback(MovieDetail detail);
    }

    public static void getMovieDetail(final Movie movie, final MovieDetailCallback movieDetailCallback) {
        String urlString = getDetailEndpoint(movie.id);
        try {
            new AsyncTask<URL, Void, MovieDetail>() {
                @Override
                protected MovieDetail doInBackground(URL... urls) {
                    return getMovieDetailFromURL(urls[0]);
                }

                @Override
                protected void onPostExecute(MovieDetail movieDetail) {
                    movieDetail.favorite = movie.favorite;
                    movieDetailCallback.callback(movieDetail);
                }
            }.execute(new URL(urlString));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static MovieDetail getMovieDetailFromURL(URL url) {
        try {
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
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
