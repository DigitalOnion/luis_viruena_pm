package com.outerspace.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.model.api.Movie;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailView {
    private MovieDetailPresenter presenter;

    private Toolbar toolbar;
    private ImageView imageMoviePoster;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView textOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(MainPresenter.MOVIE);
        presenter = new MovieDetailPresenter(this, movie);

        initReferenes();
        setSupportActionBar(toolbar);

        presenter.initValues();
    }

    private void initReferenes() {
        toolbar = findViewById(R.id.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ConstraintLayout includedLayout = findViewById(R.id.include_collapsing_toolbar_detail);
        imageMoviePoster = includedLayout.findViewById(R.id.movie_poster_image);

        NestedScrollView includedScroll = findViewById(R.id.include_body_detail);
        textOverview = includedScroll.findViewById(R.id.overview);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.details_option:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public TextView getTextOverview() {
        return textOverview;
    }

    @Override
    public ImageView getImageMoviePoster() {
        return imageMoviePoster;
    }

    @Override
    public CollapsingToolbarLayout getCollapsingToolbar() {
        return collapsingToolbar;
    }
}
