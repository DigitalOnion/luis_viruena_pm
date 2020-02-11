package com.outerspace.movies;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.outerspace.movies.model.persistence.MovieDatabase;

public interface MovieDetailView {
    Toolbar getToolbar();
    ImageView getImageMoviePoster();
    CollapsingToolbarLayout getCollapsingToolbar();
    TextView getTextHeader();
    TextView getTextOverview();
    ImageButton getMarkAsFavoriteButton();
}
