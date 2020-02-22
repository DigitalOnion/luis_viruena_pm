package com.outerspace.movies.api;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie implements Parcelable {

    @PrimaryKey public Integer id;

    public Double popularity;
    public Integer voteCount;
    public Boolean video;
    public String posterPath;
    public Boolean adult;
    public String backdropPath;
    public String originalLanguage;
    public String originalTitle;
    //public List<Integer> genreIds = null;
    public String title;
    public Integer voteAverage;
    public String overview;
    public String releaseDate;

    // for stage 2, the user can mark a movie as favorite. This is not a field from the API.
    public boolean favorite;

    public Movie() {
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Movie)) return false;

        Movie otherMovie = (Movie) obj;
        if(!(id == otherMovie.id)) return false;
        if(!(popularity == otherMovie.popularity)) return false;
        if(!(voteCount == otherMovie.voteCount)) return false;
        if(!(video == otherMovie.video)) return false;
        if(!(posterPath == otherMovie.posterPath)) return false;
        if(!(adult == otherMovie.adult)) return false;
        if(!(backdropPath.equals(otherMovie.backdropPath))) return false;
        if(!(originalLanguage.equals(otherMovie.originalLanguage))) return false;
        if(!(originalTitle.equals(otherMovie.originalTitle))) return false;
        if(!(title.equals(otherMovie.title))) return false;
        if(!(voteAverage.equals(otherMovie.voteAverage))) return false;
        if(!(overview.equals(otherMovie.overview))) return false;
        if(!(releaseDate.equals(otherMovie.releaseDate))) return false;
        return true;
    }

    // Automatic implementation of Parcelable by Android Studio.

    protected Movie(Parcel in) {
        if (in.readByte() == 0) {
            popularity = null;
        } else {
            popularity = in.readDouble();
        }
        if (in.readByte() == 0) {
            voteCount = null;
        } else {
            voteCount = in.readInt();
        }
        byte tmpVideo = in.readByte();
        video = tmpVideo == 0 ? null : tmpVideo == 1;
        posterPath = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        byte tmpAdult = in.readByte();
        adult = tmpAdult == 0 ? null : tmpAdult == 1;
        backdropPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        title = in.readString();
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readInt();
        }
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (popularity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(popularity);
        }
        if (voteCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(voteCount);
        }
        dest.writeByte((byte) (video == null ? 0 : video ? 1 : 2));
        dest.writeString(posterPath);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeByte((byte) (adult == null ? 0 : adult ? 1 : 2));
        dest.writeString(backdropPath);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(title);
        if (voteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(voteAverage);
        }
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }
}
