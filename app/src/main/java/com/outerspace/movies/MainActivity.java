package com.outerspace.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.outerspace.movies.databinding.ActivityMainBinding;
import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.model.persistence.MovieRepository;
import com.outerspace.movies.view.MainView;
import com.outerspace.movies.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements MainView {
    public static final String MOVIE = "movie";
    private static final int GRID_COLUMN_COUNT = 3;
    private RecyclerView recycler;
    private ProgressBar progressBar;
    private MainViewModel viewModel;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieRepository.initialize(this.getApplicationContext());      // initialize movie repository

        progressBar = findViewById(R.id.progress);
        recycler = findViewById(R.id.grid_recycler);
        recycler.setLayoutManager(new GridLayoutManager(this, GRID_COLUMN_COUNT));

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getMutableOptionSelected().observe(this, this::fetchMoviesOnItemId);
        viewModel.setMainView(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(viewModel);

        if (viewModel.getMutableOptionSelected().getValue() == MainViewModel.INVALID_OPTION) {
            viewModel.getMutableOptionSelected().setValue(R.id.sort_most_popular);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        int option = viewModel.getMutableOptionSelected().getValue();
        viewModel.getMutableOptionSelected().setValue(option);          // refresh after coming back from detail view
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.sort_most_popular:
            case R.id.sort_highest_rated:
            case R.id.sort_favorites:
                viewModel.getMutableOptionSelected().setValue(itemId);
                return true;
            case R.id.settings_clear_favorites:
                clearFavorites(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean fetchMoviesOnItemId(int itemId) {
        switch (itemId) {
            case R.id.sort_most_popular:
                binding.progress.setVisibility(View.VISIBLE);
                MovieModel.getPopularMovies(movies -> {
                    viewModel.getMutableMovieList().setValue(movies);
                    binding.progress.setVisibility(View.INVISIBLE);
                });
                return true;
            case R.id.sort_highest_rated:
                binding.progress.setVisibility(View.VISIBLE);
                MovieModel.getTopRatedMovies(movies -> {
                    viewModel.getMutableMovieList().setValue(movies);
                    binding.progress.setVisibility(View.INVISIBLE);
                });
                return true;
            case R.id.sort_favorites:
                binding.progress.setVisibility(View.VISIBLE);
                MovieRepository.getFavoriteMovies(movies -> {
                    viewModel.getMutableMovieList().setValue(movies);
                    binding.progress.setVisibility(View.INVISIBLE);
                });
                return true;
            default:
                return false;
        }
    }

    private void clearFavorites(Context context) {
        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MovieRepository.clearAllMoviesAsync(voidVariable -> viewModel.getMutableOptionSelected().setValue(R.id.sort_most_popular));
            }
        };
        new AlertDialog.Builder(context)
                .setTitle(R.string.clear_favorites_title)
                .setMessage(R.string.clear_favorites_message)
                .setPositiveButton(R.string.clear_favorites_clear, positiveListener)
                .setNegativeButton(R.string.clear_favorites_cancel, null)
                .create()
                .show();
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
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
