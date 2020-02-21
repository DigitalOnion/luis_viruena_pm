package com.outerspace.movies;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.model.api.BaseMedia;
import com.outerspace.movies.model.api.Movie;
import com.outerspace.movies.model.api.Review;
import com.outerspace.movies.model.api.Trailer;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailView, TrailerView {
    private MovieDetailPresenter presenter;
    private MediaPresenter mediaPresenter;

    private Toolbar toolbar;
    private ImageView imageMoviePoster;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView textHeader;
    private TextView textOverview;
    private ImageButton buttonMarkAsFavorite;
    private TextView textMarkAsFavorite;
    private ProgressBar progressBar;

    private RecyclerView trailerRecycler;
    private MediaAdapter mediaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(MainPresenter.MOVIE);
        presenter = new MovieDetailPresenter(this);

        setupActionBar();
        initReferences();

        presenter.presentMovieDetail(movie);
        mediaPresenter = new MediaPresenter(this);

        trailerRecycler.setLayoutManager(new LinearLayoutManager(this));
        mediaAdapter = new MediaAdapter(mediaPresenter);
        trailerRecycler.setAdapter(mediaAdapter);
        mediaPresenter.fetchTrailers(movie.id, new MediaPresenter.MediaCallback<Trailer>() {
            @Override
            public void call(List<Trailer> resultTrailerList) {
                List<BaseMedia> list = new ArrayList<>();
                for(Trailer trailer : resultTrailerList) list.add(trailer);
                mediaAdapter.addAllMedia(list);
            }
        });
        mediaPresenter.fetchReviews(movie.id, new MediaPresenter.MediaCallback<Review>() {
            @Override
            public void call(List<Review> resultReviewList) {
                List<BaseMedia> list = new ArrayList<>();
                for(Review review : resultReviewList) list.add(review);
                mediaAdapter.addAllMedia(list);
            }
        });
    }

    private void setupActionBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initReferences() {
        ConstraintLayout includedLayout = findViewById(R.id.include_collapsing_toolbar_detail);
        imageMoviePoster = includedLayout.findViewById(R.id.movie_poster_image);
        textHeader = includedLayout.findViewById(R.id.header_text);
        View.OnClickListener favoriteListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onMarkAsFavorite();
            }
        };
        buttonMarkAsFavorite = includedLayout.findViewById(R.id.favorite_button);
        buttonMarkAsFavorite.setOnClickListener(favoriteListener);
        textMarkAsFavorite = includedLayout.findViewById(R.id.favorite_text);
        textMarkAsFavorite.setOnClickListener(favoriteListener);

        NestedScrollView includedScroll = findViewById(R.id.include_body_detail);
        textOverview = includedScroll.findViewById(R.id.overview);
        trailerRecycler = includedScroll.findViewById(R.id.trailer_recycler);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar_layout);
        progressBar = findViewById(R.id.progress);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override public Toolbar getToolbar() { return toolbar; }

    @Override public TextView getTextOverview() { return textOverview; }

    @Override public ImageView getImageMoviePoster() { return imageMoviePoster; }

    @Override public CollapsingToolbarLayout getCollapsingToolbar() { return collapsingToolbar; }

    @Override public TextView getTextHeader() { return textHeader; }

    @Override public ImageButton getMarkAsFavoriteButton() { return buttonMarkAsFavorite; }

    @Override public ProgressBar getProgressBar() { return progressBar; }

    @Override
    public void showTrailer(Trailer trailer) {
        // https://www.youtube.com/watch?v=SYLQdxec5lM
        String urlString = null;
        switch (trailer.site.toUpperCase()) {
            case "YOUTUBE":
                urlString = MovieModel.getYoutubeEndpoint(trailer.key);
                break;
        }
        if(urlString != null) {
            Uri webpage = Uri.parse(urlString);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}
