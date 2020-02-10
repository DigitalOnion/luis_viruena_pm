package com.outerspace.movies.model;

public class BaseMovieModel {
    public static final String POSTER_SIZE = "w185";
    public static final String POSTER_SIZE_BIG = "w780";

    public static final String API_KEY = "api_key";
    public static final String LANGUAGE = "language";
    public static final String PAGE = "page";

    public static final String API_KEY_VALUE = "6ede68890852274974d5af2c4941796e";
    public static final String US_ENGLISH = "en-US";

    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String TOP_RATED_ENDPOINT = "movie/top_rated";
    public static final String POPULAR_ENDPOINT = "movie/popular";

    private static final String DETAIL_ENDPOINT = "movie/";

    public static final String POSTER_BASE_URL = "http://image.tmdb.org/";
    public static final String POSTER_ENDPOINT = "t/p/";


    public static String getApiKey() { return API_KEY + "=" + API_KEY_VALUE; }

    public static String getLanguage() { return LANGUAGE + "=" + US_ENGLISH; }

    public static String getPage(int page) { return PAGE + "=" + page; }

    public static String getDetailEndpoint(int idMovie) {
        return API_BASE_URL + DETAIL_ENDPOINT + idMovie + "?"
                + getApiKey() + "&"
                + getLanguage() + "&"
                + getPage(1);
    }
}
