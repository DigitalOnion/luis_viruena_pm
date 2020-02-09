package com.outerspace.movies;

import android.view.Gravity;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.model.api.Movie;

public class MovieDetailPresenter {
    MovieDetailView detailView;
    Movie movie;

    public MovieDetailPresenter(MovieDetailView detailView, Movie movie) {
        this.movie = movie;
        this.detailView = detailView;
    }

    void initValues() {
        String imageUrl = MovieModel.getPosterPathURL(MovieModel.POSTER_SIZE_BIG, movie.posterPath);

        detailView.getCollapsingToolbar().setTitle(movie.originalTitle);
        detailView.getCollapsingToolbar().setExpandedTitleGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

        Glide.with(detailView.getImageMoviePoster().getContext()).load(imageUrl).fitCenter().into(detailView.getImageMoviePoster());
        detailView.getTextOverview().setText(movie.overview);
//        ((TextView)findViewById(R.id.release_date)).setText(movie.releaseDate);
//        ((TextView)findViewById(R.id.vote_average)).setText(String.valueOf(movie.voteAverage));
    }

}
