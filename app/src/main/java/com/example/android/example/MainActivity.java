package com.example.android.example;

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.example.utilities.NetworkUtilities;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private static final int SPAN_COUNT = 3;
    private ArrayList<Movie> moviesList;
    private static final String KEY_PARCEL_MOVIE_LIST = "movies_list";
    private String sortByPath;
    private URL parseUrl;
    private TextView errorMessageTV;
    private ProgressBar progressBar;
    private int mPosition = RecyclerView.NO_POSITION;
    private static SQLiteDatabase mDb;
    private FavMoviesAdapter favMoviesAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        moviesList = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        errorMessageTV = (TextView) findViewById(R.id.tv_error_message);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MoviesAdapter(new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie position) {
                String movie_title = position.getTitle();
                Toast.makeText(MainActivity.this, movie_title, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);


        if (sortByPath == null) {
            parseUrl = NetworkUtilities.getDefaultSortByPathUrl();
        }

        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_PARCEL_MOVIE_LIST)) {
            new moviesDBQueryTask().execute(parseUrl);
        } else {
            moviesList = savedInstanceState.getParcelableArrayList(KEY_PARCEL_MOVIE_LIST);
            adapter.setMovies(moviesList);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_PARCEL_MOVIE_LIST, moviesList);
        super.onSaveInstanceState(outState);
    }


    private void showMovieData() {
        errorMessageTV.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage() {
        errorMessageTV.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void showOnlyLoading() {
        errorMessageTV.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }


    public class moviesDBQueryTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showOnlyLoading();
        }

        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            if (params.length == 0) {
                return null;
            }
            URL parseUrl = params[0];
            if (NetworkUtilities.isOnline()) {
                try {
                    String json = NetworkUtilities.getResponseFromHttpUrl(parseUrl);
                    return NetworkUtilities.parseMovieJson(json);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> MovieQueryResult) {
            progressBar.setVisibility(View.INVISIBLE);
            if (MovieQueryResult != null) {
                moviesList = MovieQueryResult;
                adapter.setMovies(MovieQueryResult);
                showMovieData();
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        NetworkUtilities n = new NetworkUtilities();
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.highest_rating) {
            Context context = MainActivity.this;
            String textToShow = "Highest Rating Movies";
            sortByPath = "top_rated";
            recyclerView.setAdapter(adapter);
            loadMovies(sortByPath);
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemThatWasClickedId == R.id.most_popular) {
            Context context = MainActivity.this;
            String textToShow = "Most Popular Movies";
            sortByPath = "popular";
            loadMovies(sortByPath);
            recyclerView.setAdapter(adapter);
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemThatWasClickedId == R.id.favourite) {
            Context context = MainActivity.this;
            String textToShow = "Most Popular Movies";
            FavListDBHelper dbHelper = new FavListDBHelper(this);
            mDb = dbHelper.getWritableDatabase();
//            TestUtil.insertFakeData(mDb);
            Cursor cursor = getAllGuests();
            favMoviesAdapter = new FavMoviesAdapter(this, cursor);
            recyclerView.setAdapter(favMoviesAdapter);
            return true;
        }
        new moviesDBQueryTask().execute(parseUrl);
        adapter.setMovies(moviesList);

        return super.onOptionsItemSelected(item);

    }

    private void loadMovies(final String sortByPath) {
        moviesList.clear();
        showOnlyLoading();
        if (sortByPath != null)
            parseUrl = NetworkUtilities.getSortByPathUrl(sortByPath);
        new moviesDBQueryTask().execute(parseUrl);

        adapter.setMovies(moviesList);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        recyclerView.scrollToPosition(mPosition);
        displayUI(moviesList);

    }

    private void displayUI(List<Movie> movieLists) {
        if (movieLists != null && movieLists.size() != 0) {
            showMovieData();
        } else {
            showErrorMessage();
        }
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
}


