package com.outerspace.movies;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.outerspace.movies.model.MovieDetailModel;
import com.outerspace.movies.model.MovieModel;
import com.outerspace.movies.model.UiWaitingCallback;
import com.outerspace.movies.model.api.Movie;
import com.outerspace.movies.model.api.MovieDetail;

public class MovieDetailPresenter {
    MovieDetailView detailView;

    private UiWaitingCallback waitingCallback = new UiWaitingCallback() {
        @Override
        public void callback(boolean uiWaiting) {
            // Todo: Implement
        }
    };

    private MovieDetailModel.MovieDetailCallback detailCallback = new MovieDetailModel.MovieDetailCallback() {
        @Override
        public void callback(MovieDetail detail) {
            initValues(detail);
        }
    };


    public MovieDetailPresenter(MovieDetailView detailView) {
        this.detailView = detailView;
    }

    void presentMovieDetail(Movie movie) {
        MovieDetailModel.getMovieDetail(movie.id, waitingCallback, detailCallback);
    }

    void initValues(MovieDetail detail) {
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
//        ((TextView)findViewById(R.id.release_date)).setText(movie.releaseDate);
//        ((TextView)findViewById(R.id.vote_average)).setText(String.valueOf(movie.voteAverage));
    }
}
