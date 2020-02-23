package com.outerspace.movies.view;

import android.content.Context;
import android.widget.ProgressBar;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

public interface MainView {
    static final int GRID_SPAN_COUNT = 3;

    Context getContext();
    LifecycleOwner getLifecycleOwner();
    RecyclerView getRecycler();
    ProgressBar getProgressBar();
}
