package com.outerspace.movies;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.outerspace.movies.databinding.MovieGridItemBinding;
import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.api.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.outerspace.movies.MainActivity.MOVIE;

public class MainGridAdapter extends RecyclerView.Adapter<MainGridAdapter.MovieViewHolder> {
    private List<Movie> movies = new ArrayList<>();

    public MainGridAdapter(LifecycleOwner owner, MutableLiveData<List<Movie>> mutableMovieList) {
        mutableMovieList.observe(owner,
                movieList -> {
                    this.movies.clear();
                    this.movies.addAll(movieList);
                    this.notifyDataSetChanged();
                });
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieGridItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.movie_grid_item, parent, false);
        MovieViewHolder holder = new MovieViewHolder(binding.getRoot());
        holder.binding = binding;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (movies != null) {
            holder.movie = movies.get(position);
            holder.binding.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), MovieDetailActivity.class);
                intent.putExtra(MOVIE, holder.movie);
                view.getContext().startActivity(intent);
            });
            String posterPathUrl = MovieModel.getPosterPathURL(holder.movie.posterPath);
            if (URLUtil.isValidUrl(posterPathUrl)) {
                Glide
                        .with(holder.binding.imageItem.getContext())
                        .load(posterPathUrl)
                        .fitCenter()
                        .into(holder.binding.imageItem);
            }
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        Movie movie;
        MovieGridItemBinding binding;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
