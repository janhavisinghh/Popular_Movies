package com.example.android.example.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import static com.example.android.example.Database.MoviesContract.MoviesEntry.COLUMN_MOVIE_ID;
import static com.example.android.example.Database.MoviesContract.MoviesEntry.TABLE_NAME;
import static com.example.android.example.Database.MoviesContract.MoviesEntry.buildTodoUriWithId;

public class FavMoviesContentProvider extends ContentProvider {
    public static final int FAV_MOVS = 100;
    public static final int FAV_MOVS_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavListDBHelper favListDBHelper;

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.AUTHORITY;


        uriMatcher.addURI(authority, TABLE_NAME, FAV_MOVS);
        uriMatcher.addURI(authority, TABLE_NAME + "/#", FAV_MOVS_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        favListDBHelper = new FavListDBHelper(context);
        return true;
    }

    /**
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = favListDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        switch (match) {
            case FAV_MOVS:
                long id = db.insert(TABLE_NAME, null, values);
                if (id != -1) {
                    try {
                        getContext().getContentResolver().notifyChange(uri, null);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                return buildTodoUriWithId(id);

            default:
                return null;
        }
    }

    /**
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = favListDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case FAV_MOVS_WITH_ID:
                String _ID = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{_ID};

                retCursor = favListDBHelper.getReadableDatabase().query(
                        TABLE_NAME,
                        projection,
                        COLUMN_MOVIE_ID + "=?",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;


            case FAV_MOVS:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        try {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return retCursor;
    }

    /**
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = favListDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int tasksDeleted;
        switch (match) {
            case FAV_MOVS:
                tasksDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case FAV_MOVS_WITH_ID:
                String movie_id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(TABLE_NAME, COLUMN_MOVIE_ID + "="
                        + movie_id
                        + (!TextUtils.isEmpty(selection) ? " AND ("
                        + selection + ')' : ""), new String[]{movie_id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0) {
            try {
                getContext().getContentResolver().notifyChange(uri, null);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        return tasksDeleted;
    }

    /**
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * @param uri
     * @return
     */
    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

}
