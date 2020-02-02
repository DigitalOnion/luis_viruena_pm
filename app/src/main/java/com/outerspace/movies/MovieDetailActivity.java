package com.outerspace.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.model.api.Movie;

public class MovieDetailActivity extends AppCompatActivity {
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        movie = intent.getParcelableExtra(MainPresenter.MOVIE);

        // this is simple enough that does not need a presenter
        ((TextView)findViewById(R.id.movie_original_title)).setText(movie.originalTitle);
        Glide.with(this).load(MovieModel.getPosterPathURL(MovieModel.POSTER_SIZE_BIG, movie.posterPath)).fitCenter().into((ImageView) findViewById(R.id.movie_poster_image));
        ((TextView)findViewById(R.id.overview)).setText(movie.overview);
        ((TextView)findViewById(R.id.release_date)).setText(movie.releaseDate);
        ((TextView)findViewById(R.id.vote_average)).setText(String.valueOf(movie.voteAverage));
    }
}
