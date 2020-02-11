package com.outerspace.movies;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.outerspace.movies.model.MovieDetailModel;
import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.model.UiWaitingCallback;
import com.outerspace.movies.model.api.Movie;
import com.outerspace.movies.model.api.MovieDetail;
import com.outerspace.movies.model.persistence.MovieRepository;

import java.lang.ref.WeakReference;

public class MovieDetailPresenter {
    MovieDetailView detailView;
    Movie movie;

//    private final UiWaitingCallback waitingCallback = new UiWaitingCallback() {
//        @Override
//        public void callback(boolean uiWaiting) {
//            // Todo: Implement
//        }
//    };


    private void activateProgressBar(boolean active) {
        detailView.getProgressBar().setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    private final MovieDetailModel.MovieDetailCallback detailCallback = new MovieDetailModel.MovieDetailCallback() {
        @Override
        public void callback(MovieDetail detail) {
            chooseMarkedAsFavoriteIcon(detailView, detail.favorite);
            initValues(detail);
            activateProgressBar(false);
        }
    };


    public MovieDetailPresenter(MovieDetailView detailView) {
        this.detailView = detailView;
    }

    static class PresentMovieDetailTask extends AsyncTask<Movie, Void, Movie> {
        private MovieDetailView detailView;
        private Movie movie;
        private MovieDetailModel.MovieDetailCallback detailCallback;

        public PresentMovieDetailTask(MovieDetailView detailView, Movie movie, MovieDetailModel.MovieDetailCallback detailCallback) {
            this.detailView = detailView;
            this.movie = movie;
            this.detailCallback = detailCallback;
        }

        @Override
        protected Movie doInBackground(Movie... movies) {
            Movie movie = movies[0];
            if (MovieRepository.getInstance().isMovieStored(movie.id)) {
                movie = MovieRepository.getInstance().getMovieFromId(movie.id);
            } else {
                MovieRepository.getInstance().insert(movie);
                MovieRepository.getInstance().flipFavorite(movie.id);
            }
            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            MovieDetailModel.getMovieDetail(movie, detailCallback);
        }
    }

    void presentMovieDetail(Movie movie) {
        activateProgressBar(true);
        this.movie = movie;
        PresentMovieDetailTask task = new PresentMovieDetailTask(detailView, movie, detailCallback);
        task.execute(movie);
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
        MovieRepository.getInstance().flipFavoriteAsync(movie.id, new MovieRepository.MovieRepositoryCallback <Boolean> () {
            @Override
            public void call (Boolean favorite){
                movie.favorite = favorite;
                chooseMarkedAsFavoriteIcon(detailView, movie.favorite);
            }
        });
    }
}
