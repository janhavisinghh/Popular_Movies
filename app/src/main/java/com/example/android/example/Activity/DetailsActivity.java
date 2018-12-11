package com.example.android.example.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.example.Adapters.FavMoviesAdapter;
import com.example.android.example.AppExecutors;
import com.example.android.example.Database.FavListDBHelper;
import com.example.android.example.Database.MoviesContract;
import com.example.android.example.R;
import com.squareup.picasso.Picasso;

import static com.example.android.example.Database.MoviesContract.MoviesEntry.COLUMN_MOVIE_ID;
import static com.example.android.example.Database.MoviesContract.MoviesEntry.CONTENT_URI;

public class DetailsActivity extends AppCompatActivity {
    public static final String BASE_PATH = "http://image.tmdb.org/t/p/w500";
    private static final String KEY_PARCEL = "selected_movie";
    public String title;
    public String synopsis;
    public String rating;
    public String date;
    public String poster;
    public String movie_id;
    TextView title_tv;
    TextView synopsis_tv;
    TextView rating_tv;
    TextView date_tv;
    ImageView poster_iv;
    Button trailer;
    Button review_button;
    RelativeLayout relativeLayout;
    private SQLiteDatabase mDb;
    private FavMoviesAdapter favMoviesAdapter;
    private int _ID = -1;
    private Menu menu;
    private FloatingActionButton fab;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        FavListDBHelper dbHelper = new FavListDBHelper(this);
        mDb = dbHelper.getWritableDatabase();
        title_tv = findViewById(R.id.title_tv_detail);
        synopsis_tv = findViewById(R.id.synopsis_tv_detail);
        rating_tv = findViewById(R.id.rating_tv_detail);
        date_tv = findViewById(R.id.release_tv_detail);
        poster_iv = findViewById(R.id.poster_iv_detail);
        trailer = (Button) findViewById(R.id.trailer_button);
        review_button = (Button) findViewById(R.id.reviews_button);
        fab = (FloatingActionButton) findViewById(R.id.favourite_button);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_da);


        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        synopsis = intent.getExtras().getString("overview");
        rating = intent.getExtras().getString("vote_average");
        date = intent.getExtras().getString("release_date");
        poster = intent.getExtras().getString("poster_path");
        movie_id = intent.getExtras().getString("id");

        if (searchMovieInDB(movie_id))
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryAccent)));
        else
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLight)));

        setTitle(title);

        review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DetailsActivity.this, ReviewActivity.class);
                myIntent.putExtra("movie_id", movie_id);
                myIntent.putExtra("title", title);
                startActivity(myIntent);
            }
        });
        trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DetailsActivity.this, TrailerActivity.class);
                myIntent.putExtra("movie_id", movie_id);
                myIntent.putExtra("title", title);
                startActivity(myIntent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            boolean addToFavourite = searchMovieInDB(movie_id);

            @Override
            public void onClick(View view) {
                if (addToFavourite) {
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLight)));
                    addToFavourite = false;
                    removeGuest(movie_id);

                } else {
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryAccent)));
                    addToFavMovieList();
                    addToFavourite = true;
                }
            }
        });


        title_tv.setText(title);
        synopsis_tv.setText(synopsis);
        rating_tv.setText(rating);
        date_tv.setText(date);


        if (poster.equals("null")) {
            poster_iv.setImageResource(R.drawable.clear_button);
        } else {
            String posterUrl = BASE_PATH + poster;
            Picasso.get().load(posterUrl).fit().centerCrop().into(poster_iv);
        }


    }

    /**
     * @param title
     * @param poster
     * @param overview
     * @param userRating
     * @param releaseDate
     * @param movieID
     */
    private void addNewFavMovie(String title, String poster, String overview, String userRating,
                                String releaseDate, String movieID) {
        final ContentValues cv = new ContentValues();
        cv.put(MoviesContract.MoviesEntry.COLUMN_TITLE, title);
        cv.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, poster);
        cv.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, overview);
        cv.put(MoviesContract.MoviesEntry.COLUMN_USER_RATING, userRating);
        cv.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, releaseDate);
        cv.put(COLUMN_MOVIE_ID, movieID);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, cv);
            }
        });


    }


    public void addToFavMovieList() {
        Snackbar snackbar = Snackbar
                .make(relativeLayout, title + " Added!", Snackbar.LENGTH_SHORT);

        snackbar.show();
        addNewFavMovie(title, poster, synopsis, rating, date, movie_id);
        Cursor cursor = getAllMovies();
        favMoviesAdapter = new FavMoviesAdapter(this, cursor);
        favMoviesAdapter.swapCursor(getAllMovies());
    }

    /**
     * @param movie_id
     */
    private void removeGuest(final String movie_id) {
        Snackbar snackbar = Snackbar
                .make(relativeLayout, title + " Removed!", Snackbar.LENGTH_SHORT);

        snackbar.show();
        FavListDBHelper dbHelper = new FavListDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                getContentResolver().delete(CONTENT_URI, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?", new String[]{movie_id});

            }
        });
    }

    /**
     * @param movie_id
     * @return
     */
    public boolean searchMovieInDB(String movie_id) {
        String[] projection = {
                MoviesContract.MoviesEntry.COLUMN_TITLE,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
                MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
                MoviesContract.MoviesEntry.COLUMN_USER_RATING,
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW
        };
        String selection = COLUMN_MOVIE_ID + " =?";
        String[] selectionArgs = {movie_id};
        String limit = "1";
        long id = 2;

        Cursor cursor = getContentResolver().query(MoviesContract.MoviesEntry.buildTodoUriWithId(id),
                projection,
                selection,
                selectionArgs,
                limit);
        boolean movie_found = (cursor.getCount() > 0);
        cursor.close();
        return movie_found;
    }

    public Cursor getAllMovies() {
        return getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID);
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int index = -1;
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

}



