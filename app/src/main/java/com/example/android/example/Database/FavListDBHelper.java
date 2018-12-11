package com.example.android.example.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavListDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favmovlist.db";

    private static final int DATABASE_VERSION = 2;

    /**
     * @param context
     */
    public FavListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT  , " +
                MoviesContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT , " +
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT , " +
                MoviesContract.MoviesEntry.COLUMN_USER_RATING + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT , " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " TEXT PRIMARY KEY " + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    /**
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
