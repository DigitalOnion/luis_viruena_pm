package com.outerspace.movies.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import com.outerspace.movies.api.Movie;
import com.outerspace.movies.model.persistence.MovieDao;
import com.outerspace.movies.model.persistence.MovieDatabase;
import com.outerspace.movies.model.persistence.MovieRepository;

import static com.outerspace.movies.model.MovieModel.getMovieFromJson;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(RobolectricTestRunner.class)
public class MovieRepositoryTest {

    private static final String JSON_MOVIE_AD_ASTRA = "{\"movie\": {\n" +
            "      \"popularity\": 366.496,\n" +
            "      \"vote_count\": 2294,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg\",\n" +
            "      \"id\": 419704,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/5BwqwxMEjeFtdknRV792Svo0K1v.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Ad Astra\",\n" +
            "      \"genre_ids\": [\n" +
            "        12,\n" +
            "        18,\n" +
            "        9648,\n" +
            "        878,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"title\": \"Ad Astra\",\n" +
            "      \"vote_average\": 6,\n" +
            "      \"overview\": \"The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.\",\n" +
            "      \"release_date\": \"2019-09-17\"\n" +
            "    } }";

    private Movie getTestMovie() {
        try {
            JSONObject jsonMovie = new JSONObject(JSON_MOVIE_AD_ASTRA).getJSONObject("movie");
            Movie movie = getMovieFromJson(jsonMovie);
            return movie;
        } catch (JSONException e) {
            return null;
        }
    }

    @Mock
    MovieDao mockMovieDao;
    @Mock
    MovieDatabase mockMovieDatabase;

    @Before
    public void createDB() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockMovieDatabase.movieDao()).thenReturn(mockMovieDao);
        MovieRepositoryForTest.initialize(mockMovieDatabase);
    }

    @Test
    public void movieFromJsonTest() {
        Movie movie = getTestMovie();
        Assert.assertNotNull(movie);
        Assert.assertNotNull(movie.title);
        Assert.assertNotNull(movie.overview);
        Assert.assertNotNull(movie.posterPath);
    }

    @Test
    public void movieRepositoryInsertAndGetMovieFromId() {

        Movie movie = getTestMovie();
        MovieRepositoryForTest.getInstance().insert(movie);
        Movie movieFromDB = MovieRepositoryForTest.getInstance().getMovieFromId(movie.id);

        Mockito.verify(mockMovieDao).insert(movie);
        Mockito.verify(mockMovieDao).getMovieFromId(movie.id);
        
    }

    private static class MovieRepositoryForTest extends MovieRepository {
        public static void initialize(MovieDatabase movieDatabase) {
            instance = new MovieRepositoryForTest();
            MovieRepository.movieDatabase = movieDatabase;
        }
    }
}
