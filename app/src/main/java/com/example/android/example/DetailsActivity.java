package com.example.android.example;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.ArrayList;



public class DetailsActivity extends AppCompatActivity {
    private static final String KEY_PARCEL = "selected_movie";
    TextView title_tv;
    TextView synopsis_tv;
    TextView rating_tv;
    TextView date_tv;
    ImageView poster_iv;
    Button trailer;
    Button review_button;
    TextView reviewer_name;
    TextView review;


    public String title;
    public String synopsis;
    public String rating;
    public String date;
    public String poster;
    public String movie_id;
    private SQLiteDatabase mDb;
    private FavMoviesAdapter favMoviesAdapter;


    public ArrayList<Review> reviews;
    public Review review_obj;


    public static final String BASE_PATH = "http://image.tmdb.org/t/p/w500";
    public static final String YT_BASE_PATH ="https://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        FavListDBHelper dbHelper = new FavListDBHelper(this);
        mDb = dbHelper.getWritableDatabase();
        title_tv = findViewById(R.id.title_tv_detail);
        synopsis_tv =findViewById(R.id.synopsis_tv_detail);
        rating_tv = findViewById(R.id.rating_tv_detail);
        date_tv =  findViewById(R.id.release_tv_detail);
        poster_iv =  findViewById(R.id.poster_iv_detail);
        trailer = (Button) findViewById(R.id.trailer_button);
        review_button = (Button) findViewById(R.id.reviews_button);
        reviewer_name = (TextView) findViewById(R.id.reviewer_name);
        review = (TextView) findViewById(R.id.review);


         review_obj = new Review();


        reviews = new ArrayList<Review>();



        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        synopsis = intent.getExtras().getString("overview");
        rating = intent.getExtras().getString("vote_average");
        date = intent.getExtras().getString("release_date");
        poster = intent.getExtras().getString("poster_path");
        movie_id = intent.getExtras().getString("id");

        final URL trailerUrl = NetworkUtilities.buildTrailerUrl(movie_id);
        final URL reviewUrl = NetworkUtilities.buildReviewURL(movie_id);


        title_tv.setText(title);
        synopsis_tv.setText(synopsis);
        rating_tv.setText(rating);
        date_tv.setText(date);


        new trailerQueryTask().execute(trailerUrl);
        new reviewsQueryTask().execute(reviewUrl);



        if (poster.equals("null")) {
            poster_iv.setImageResource(R.drawable.no_image);
        } else {
            String posterUrl = BASE_PATH + poster;
            Picasso.get().load(posterUrl).fit().centerCrop().into(poster_iv);
        }

    }
    public void hideReviewButton(){
        review_button.setVisibility(View.INVISIBLE);
        review.setVisibility(View.VISIBLE);
        reviewer_name.setVisibility(View.VISIBLE);
    }
    public void showReviewButton(){
        review_button.setVisibility(View.VISIBLE);
        review.setVisibility(View.INVISIBLE);
        reviewer_name.setVisibility(View.INVISIBLE);
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
    private long addNewFavMovie(String title, String poster, String overview, String userRating,
                                String releaseDate, String movieID) {
            ContentValues cv = new ContentValues();
            cv.put(MoviesContract.MoviesEntry.COLUMN_TITLE,title );
            cv.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, poster);
            cv.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, overview);
            cv.put(MoviesContract.MoviesEntry.COLUMN_USER_RATING, userRating);
            cv.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, releaseDate);
            cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movieID);
            return mDb.insertWithOnConflict(MoviesContract.MoviesEntry.TABLE_NAME, null, cv,SQLiteDatabase.CONFLICT_REPLACE);

    }

    public void addToFavMovieList() {

        addNewFavMovie(title,poster,synopsis,rating,date,movie_id);
        Cursor cursor = getAllGuests();
        favMoviesAdapter = new FavMoviesAdapter(this, cursor);
        favMoviesAdapter.swapCursor(getAllGuests());

    }
    public Cursor getAllGuests() {
        return mDb.query(MoviesContract.MoviesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MoviesContract.MoviesEntry._ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.favourite_button) {
            addToFavMovieList();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    public class reviewsQueryTask extends AsyncTask<URL, Void, ArrayList<Review>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showReviewButton();
        }
        @Override
        protected ArrayList<Review> doInBackground(URL... params) {
            if (params.length == 0) {
                return null;
            }
            URL parseUrl = params[0];
            if (NetworkUtilities.isOnline()) {
                try {
                    String json = NetworkUtilities.getResponseFromHttpUrl(parseUrl);
                    return NetworkUtilities.parseReviewJson(json);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<Review> MovieQueryResult) {
            review_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Reviews" , Toast.LENGTH_SHORT).show();
                    reviews = MovieQueryResult ;
                    review_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),Review.class);
                            intent.putExtra("author", review_obj.getReviewer_name());
                            intent.putExtra("content", review_obj.getReview());

                            getApplicationContext().startActivity(intent);

                        }
                    });
                   }});
        }
    }



}



