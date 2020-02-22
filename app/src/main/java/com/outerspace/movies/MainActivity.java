package com.outerspace.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.outerspace.movies.model.persistence.MovieRepository;
import com.outerspace.movies.view.MainView;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final int GRID_COLUMN_COUNT = 3;
    RecyclerView recycler;
    ProgressBar progressBar;
    MainGridAdapter adapter;

    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieRepository.initialize(this.getApplicationContext());      // initialize movie repository

        progressBar = findViewById(R.id.progress);
        recycler = findViewById(R.id.grid_recycler);
        recycler.setLayoutManager(new GridLayoutManager(this, GRID_COLUMN_COUNT));

        presenter = new MainPresenter(this);
        adapter = new MainGridAdapter(presenter);
        presenter.presentMostPopular(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_most_popular:
                presenter.presentMostPopular(adapter);
                return true;
            case R.id.sort_highest_rated:
                presenter.presentTopRated(adapter);
                return true;
            case R.id.sort_favorites:
                presenter.presentFavorites(adapter);
                return true;
            case R.id.settings_clear_favorites:
                presenter.clearFavorites(this.getContext());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public RecyclerView getRecycler() {
        return recycler;
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
