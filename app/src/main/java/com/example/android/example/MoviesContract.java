package com.example.android.example;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    protected static final String AUTHORITY = "com.example.android.example";
    protected static final String PATH_FAV_MOVIES = "FavouriteMovies";
    protected static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    protected static final class MoviesEntry implements BaseColumns{

        protected static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build();

        protected static Uri buildTodoUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }

        protected static final String TABLE_NAME = "FavouriteMovies";
        protected static final String COLUMN_TITLE = "title";
        protected static final String COLUMN_POSTER_PATH = "poster_path";
        protected static final String COLUMN_OVERVIEW = "overview";
        protected static final String COLUMN_USER_RATING = "vote_average";
        protected static final String COLUMN_RELEASE_DATE = "release_date";
        protected static final String COLUMN_MOVIE_ID = "id";

    }
}
