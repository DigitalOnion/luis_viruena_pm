package com.outerspace.movies.model.api;

import java.util.List;

public class MovieDetail {
    public Boolean adult;
    public String backdropPath;
    public Object belongsToCollection;
    public Integer budget;
    public List<Genre> genres = null;
    public String homepage;
    public Integer id;
    public String imdbId;
    public String originalLanguage;
    public String originalTitle;
    public String overview;
    public Double popularity;
    public String posterPath;
    public List<ProductionCompany> productionCompanies = null;
    public List<ProductionCountry> productionCountries = null;
    public String releaseDate;
    public Integer revenue;
    public Integer runtime;
    public List<SpokenLanguage> spokenLanguages = null;
    public String status;
    public String tagline;
    public String title;
    public Boolean video;
    public Integer voteAverage;
    public Integer voteCount;

    // for stage 2, the user can mark a movie as favorite. This is not a field from the API.
    public boolean favorite;
}