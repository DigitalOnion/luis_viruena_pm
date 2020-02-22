package com.outerspace.movies.view;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public interface MovieDetailView {
    Toolbar getToolbar();
    ImageView getImageMoviePoster();
    CollapsingToolbarLayout getCollapsingToolbar();
    TextView getTextHeader();
    TextView getTextOverview();
    ImageButton getMarkAsFavoriteButton();
    ProgressBar getProgressBar();
}
