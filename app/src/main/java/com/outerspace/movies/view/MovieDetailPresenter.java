package com.outerspace.movies.view;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.outerspace.movies.MovieDetailActivity;
import com.outerspace.movies.R;
import com.outerspace.movies.model.MovieDetailModel;
import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.api.Movie;
import com.outerspace.movies.api.MovieDetail;
import com.outerspace.movies.model.persistence.MovieRepository;
import com.outerspace.movies.view.MovieDetailView;

public class MovieDetailPresenter {
    MovieDetailView detailView;
    Movie movie;

    private void activateProgressBar(boolean active) {
        detailView.getProgressBar().setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    public MovieDetailPresenter(MovieDetailView detailView) {
        this.detailView = detailView;
    }

    static class persistMovieTask extends AsyncTask<Movie, Void, Movie> {
        private MovieDetailModel.MovieDetailCallback detailCallback;

        public persistMovieTask(MovieDetailModel.MovieDetailCallback detailCallback) {
            this.detailCallback = detailCallback;
        }

        @Override
        protected Movie doInBackground(Movie... movies) {
            Movie movie = movies[0];
            boolean favorite;
            boolean isStored = MovieRepository.getInstance().isMovieStored(movie.id);
            if (isStored) {     // recover favorite value
                favorite = MovieRepository.getInstance().isFavorite(movie.id);
            } else {
                favorite = movie.favorite;
            }

            if (isStored) {
                MovieRepository.getInstance().delete(movie);  // remove previous version of the movie
            }
            movie.favorite = favorite;      // keeps movie favorite value
            MovieRepository.getInstance().insert(movie);

            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            MovieDetailModel.fetchMovieDetail(movie, detailCallback);
        }
    }

    //static class chooseFavoriteTask extends

    public void presentMovieDetail(Movie movie) {
        activateProgressBar(true);
        this.movie = movie;
        persistMovieTask task = new persistMovieTask(new MovieDetailModel.MovieDetailCallback() {
            @Override
            public void callback(MovieDetail detail) {
                initValues(detail);
                activateProgressBar(false);
            }
        });
        task.execute(movie);

        MovieRepository.getInstance().isFavoriteAsync(movie.id, new MovieRepository.MovieRepositoryCallback <Boolean> () {
            @Override
            public void call (Boolean favorite){
                chooseMarkedAsFavoriteIcon(detailView, favorite);
            }
        });

        chooseMarkedAsFavoriteIcon(detailView, movie.favorite);
    }

    private void initValues(MovieDetail detail) {
        String imageUrl = MovieModel.getPosterPathURL(MovieModel.POSTER_SIZE_BIG, detail.posterPath);

        detailView.getCollapsingToolbar().setTitle(detail.originalTitle);
        detailView.getCollapsingToolbar().setExpandedTitleGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

        Glide.with(detailView.getImageMoviePoster().getContext())
                .load(imageUrl)
                .fitCenter()
                .into(detailView.getImageMoviePoster());

        String buffer;
        int start, end;
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
        //StyleSpan span = new StyleSpan(R.style.details_release_date);
        StyleSpan spanBold = new StyleSpan(Typeface.BOLD);
        StyleSpan spanItalic = new StyleSpan(Typeface.ITALIC);

        start = spanBuilder.length();
        buffer = detail.releaseDate;
        spanBuilder.append(buffer).append('\n');
        end = spanBuilder.length();
        spanBuilder.setSpan(spanBold, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        start = spanBuilder.length();
        buffer = String.valueOf(detail.runtime).concat(" min");
        spanBuilder.append(buffer).append('\n');
        end = spanBuilder.length();
        spanBuilder.setSpan(spanItalic, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        detailView.getTextHeader().setText(spanBuilder);

        detailView.getTextOverview().setText(detail.overview);
    }

    private void chooseMarkedAsFavoriteIcon(MovieDetailView detailView, boolean favorite) {
        Resources resources = ((MovieDetailActivity) detailView).getResources();
        Resources.Theme theme = ((MovieDetailActivity) detailView).getBaseContext().getTheme();
        Drawable drawable = resources.getDrawable(favorite
                ? R.drawable.ic_favorite
                : R.drawable.ic_favorite_border);
        detailView.getMarkAsFavoriteButton().setImageDrawable(drawable);
    }

    public void onMarkAsFavorite() {
        MovieRepository.getInstance().flipFavoriteAsync(movie, new MovieRepository.MovieRepositoryCallback <Boolean> () {
            @Override
            public void call (Boolean favorite){
                Log.d("Luis", favorite ? "favorite" : "not favorite");
                movie.favorite = favorite;
                chooseMarkedAsFavoriteIcon(detailView, movie.favorite);
            }
        });
    }
}
