package com.outerspace.movies.model.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.outerspace.movies.model.api.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insert(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("UPDATE movies SET favorite = :favorite WHERE id = :movieId")
    void updateFavorite(int movieId, boolean favorite);

    @Query("SELECT * FROM movies WHERE favorite = 1")
    List<Movie> selectFavorites();

    @Query("SELECT * FROM movies WHERE id = :movieId")
    Movie getMovieFromId(int movieId);

    @Query("SELECT Count(*) > 0 FROM movies WHERE id = :movieId")
    boolean isMovieInDB(int movieId);

    @Query("SELECT favorite FROM movies WHERE id = :movieId")
    boolean isFavoriteMovie(int movieId);
}
