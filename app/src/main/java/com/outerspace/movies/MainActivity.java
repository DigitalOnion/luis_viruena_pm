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

public class MainActivity extends AppCompatActivity implements MainView {
    private static final int GRID_COLUMN_COUNT = 3;
    RecyclerView recycler;
    ProgressBar progressBar;
    MovieGridAdapter adapter;

    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress);
        recycler = findViewById(R.id.grid_recycler);
        recycler.setLayoutManager(new GridLayoutManager(this, GRID_COLUMN_COUNT));

        presenter = new MainPresenter(this);
        adapter = new MovieGridAdapter(presenter);
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