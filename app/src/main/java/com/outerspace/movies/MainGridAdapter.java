package com.outerspace.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.api.Movie;

import java.util.List;

public class MainGridAdapter extends RecyclerView.Adapter <MainGridAdapter.MovieViewHolder> {
    private MainPresenter presenter;
    private List<Movie> movies;

    private MainGridAdapter() { }

    MainGridAdapter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    public void setMovies(List<Movie> newPopularMovies) {
        movies = newPopularMovies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if(movies != null) {
            holder.movie = movies.get(position);
            String posterPathUrl = MovieModel.getPosterPathURL(holder.movie.posterPath);
            if(URLUtil.isValidUrl(posterPathUrl)) {
                Glide
                        .with(holder.image.getContext())
                        .load(posterPathUrl)
                        .fitCenter()
                        .into(holder.image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        Movie movie;
        ImageView image;
        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_item);
            image.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             presenter.onMovieClickListener(movie);
                                         }
                                     });
        }
    }
}
