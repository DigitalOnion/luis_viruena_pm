package com.outerspace.movies.view;

import android.content.Context;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

public interface MainView {
    public Context getContext();
    public RecyclerView getRecycler();
    public ProgressBar getProgressBar();
}
