package com.example.android.example;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.example.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static com.example.android.example.MoviesContract.MoviesEntry.COLUMN_MOVIE_ID;
import static com.example.android.example.MoviesContract.MoviesEntry.CONTENT_URI;

public class DetailsActivity extends AppCompatActivity {
    private static final String KEY_PARCEL = "selected_movie";
    TextView title_tv;
    TextView synopsis_tv;
    TextView rating_tv;
    TextView date_tv;
    ImageView poster_iv;
    Button trailer;
    Button review_button;

    public String title;
    public String synopsis;
    public String rating;
    public String date;
    public String poster;
    public String movie_id;
    private SQLiteDatabase mDb;
    private FavMoviesAdapter favMoviesAdapter;
    private int _ID = -1;
    private Menu menu;


    public static final String BASE_PATH = "http://image.tmdb.org/t/p/w500";
    public static final String YT_BASE_PATH ="https://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FavListDBHelper dbHelper = new FavListDBHelper(this);
        mDb = dbHelper.getWritableDatabase();
        title_tv = findViewById(R.id.title_tv_detail);
        synopsis_tv =findViewById(R.id.synopsis_tv_detail);
        rating_tv = findViewById(R.id.rating_tv_detail);
        date_tv =  findViewById(R.id.release_tv_detail);
        poster_iv =  findViewById(R.id.poster_iv_detail);
        trailer = (Button) findViewById(R.id.trailer_button);
        review_button = (Button) findViewById(R.id.reviews_button);


        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        synopsis = intent.getExtras().getString("overview");
        rating = intent.getExtras().getString("vote_average");
        date = intent.getExtras().getString("release_date");
        poster = intent.getExtras().getString("poster_path");
        movie_id = intent.getExtras().getString("id");


        review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DetailsActivity.this, ReviewActivity.class);
                myIntent.putExtra("movie_id",movie_id);
                startActivity(myIntent);
            }
        });



        final URL trailerUrl = NetworkUtilities.buildTrailerUrl(movie_id);


        title_tv.setText(title);
        synopsis_tv.setText(synopsis);
        rating_tv.setText(rating);
        date_tv.setText(date);


        new trailerQueryTask().execute(trailerUrl);



        if (poster.equals("null")) {
            poster_iv.setImageResource(R.drawable.no_image);
        } else {
            String posterUrl = BASE_PATH + poster;
            Picasso.get().load(posterUrl).fit().centerCrop().into(poster_iv);
        }


    }
    public class trailerQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            if (params.length == 0) {
                return null;
            }
            URL parseUrl = params[0];
            if (NetworkUtilities.isOnline()) {
                try {
                    String json = NetworkUtilities.getResponseFromHttpUrl(parseUrl);
                    return NetworkUtilities.parseTrailerJSON(json);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            final String youtube_url = YT_BASE_PATH + s;
            trailer.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Trailer", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube_url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.google.android.youtube");
                    startActivity(intent);
                }
            });
        }
    }
    private void addNewFavMovie(String title, String poster, String overview, String userRating,
                                String releaseDate, String movieID) {
            final ContentValues cv = new ContentValues();
            cv.put(MoviesContract.MoviesEntry.COLUMN_TITLE,title );
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
        Toast.makeText(getApplicationContext(),title + " Added!", Toast.LENGTH_SHORT);
        addNewFavMovie(title,poster,synopsis,rating,date,movie_id);
        Cursor cursor = getAllMovies();
        favMoviesAdapter = new FavMoviesAdapter(this, cursor);
        favMoviesAdapter.swapCursor(getAllMovies());
    }
    private void removeGuest(final String movie_id) {
        Toast.makeText(getApplicationContext(),title + " Deleted!", Toast.LENGTH_SHORT);
        FavListDBHelper dbHelper = new FavListDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                getContentResolver().delete(CONTENT_URI, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?", new String[]{movie_id});

            }
        });
    }
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
        String[] selectionArgs = { movie_id };
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int index = -1;
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.favourite_button) {
            Boolean movie_present = searchMovieInDB(movie_id);
            if (movie_present)
                {   menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_red_24dp));
                    removeGuest(movie_id);
            }
            else
            {   menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_red_24dp));
                addToFavMovieList();
            }
            return true;
        }
        else if(itemThatWasClickedId== android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);

    }





}



