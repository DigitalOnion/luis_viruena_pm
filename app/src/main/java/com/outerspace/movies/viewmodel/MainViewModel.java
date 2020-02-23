package com.outerspace.movies.viewmodel;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.IntegerRes;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.outerspace.movies.MainGridAdapter;
import com.outerspace.movies.api.Movie;
import com.outerspace.movies.view.MainView;

import java.util.List;

import static com.outerspace.movies.view.MainView.GRID_SPAN_COUNT;

public class MainViewModel extends ViewModel {
    public static int INVALID_OPTION = -1;
    private MutableLiveData<Integer> mutableOptionSelected = new MutableLiveData<>(-1);
    private MutableLiveData<List<Movie>> mutableMovieList = new MutableLiveData<>();
    private static MainView mainView;

    public MutableLiveData<List<Movie>> getMutableMovieList() { return mutableMovieList; }

    public MutableLiveData<Integer> getMutableOptionSelected() { return mutableOptionSelected; }

    public void setMainView(MainView mainView) {
        MainViewModel.mainView = mainView;
    }

    public MainGridAdapter getAdapter() {
        return new MainGridAdapter(mainView.getLifecycleOwner(), mutableMovieList);
    }

    @BindingAdapter(value = "adapter", requireAll = true)
    public static void initializeRecycler(RecyclerView recyclerView, MainGridAdapter adapter) {
        recyclerView.setLayoutManager(new GridLayoutManager(mainView.getContext(),  GRID_SPAN_COUNT));
        recyclerView.setAdapter(adapter);
    }
}
