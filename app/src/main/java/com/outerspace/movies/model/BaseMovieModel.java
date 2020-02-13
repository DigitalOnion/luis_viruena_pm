package com.outerspace.movies.model;

public class BaseMovieModel {
    public static final String POSTER_SIZE = "w185";
    public static final String POSTER_SIZE_BIG = "w780";

    public static final String API_KEY = "api_key";
    public static final String LANGUAGE = "language";
    public static final String PAGE = "page";

    public static final String API_KEY_VALUE = "enter-here-your-api-key";
    public static final String US_ENGLISH = "en-US";

    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String TOP_RATED_ENDPOINT = "movie/top_rated";
    public static final String POPULAR_ENDPOINT = "movie/popular";

    private static final String DETAIL_ENDPOINT = "movie/";
    private static final String TRAILER_ENDPOINT = "movie/{movieId}/videos";

    public static final String POSTER_BASE_URL = "http://image.tmdb.org/";
    public static final String POSTER_ENDPOINT = "t/p/";

    public static final String YOUTUBE_ENDPOINT = "https://www.youtube.com/watch";

    public static String getApiKey() { return API_KEY + "=" + API_KEY_VALUE; }

    public static String getLanguage() { return LANGUAGE + "=" + US_ENGLISH; }

    public static String getPage(int page) { return PAGE + "=" + page; }

    public static String getDetailEndpoint(int idMovie) {
        return API_BASE_URL + DETAIL_ENDPOINT + idMovie + "?"
                + getApiKey() + "&"
                + getLanguage() + "&"
                + getPage(1);
    }

    public static String getTrailersEndpoint(int idMovie) {
        return API_BASE_URL + TRAILER_ENDPOINT.replace("{movieId}", String.valueOf(idMovie)) + "?"
                + getApiKey() + "&"
                + getLanguage();
    }

    public static String getYoutubeEndpoint(String youtubeKey) {
        // https://www.youtube.com/watch?v=SYLQdxec5lM
        return YOUTUBE_ENDPOINT + "?"
                + "v=" + youtubeKey;
    }
}
