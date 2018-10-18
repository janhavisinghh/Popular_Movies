package com.example.android.example;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavListDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favmovlist.db";

    private static final int DATABASE_VERSION = 1;


    public FavListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
                MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL UNIQUE, " +
                MoviesContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL UNIQUE, " +
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL UNIQUE, " +
                MoviesContract.MoviesEntry.COLUMN_USER_RATING + " TEXT NOT NULL UNIQUE, " +
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL UNIQUE, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL PRIMARY KEY UNIQUE" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
