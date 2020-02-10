package com.outerspace.movies;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.outerspace.movies.model.api.Movie;

public interface MovieDetailView {
    Toolbar getToolbar();
    ImageView getImageMoviePoster();
    CollapsingToolbarLayout getCollapsingToolbar();
    TextView getTextHeader();
    TextView getTextOverview();
}
