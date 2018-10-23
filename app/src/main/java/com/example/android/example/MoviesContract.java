package com.example.android.example;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {
    private  MoviesContract(){}

    public static final String AUTHORITY = "com.example.android.example";

    public static final class MoviesEntry implements BaseColumns{

        public static final String TABLE_NAME = "FavouriteMovies";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_USER_RATING = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_ID = "id";

    }
}
