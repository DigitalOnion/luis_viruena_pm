package com.outerspace.movies.model.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.outerspace.movies.api.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}
