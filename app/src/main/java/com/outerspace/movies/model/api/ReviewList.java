package com.outerspace.movies.model.api;

import java.util.List;

public class ReviewList {
    public Integer id;
    public Integer page;
    public List<Review> reviews = null;
    public Integer totalPages;
    public Integer totalReviews;
}