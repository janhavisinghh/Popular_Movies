package com.example.android.example;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.android.example.utilities.NetworkUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private static final int SPAN_COUNT = 3;
    private ArrayList<Movie> moviesList;
    private static final String KEY_PARCEL_INTENT = "selected_movie";
    private static final String KEY_PARCEL_MOVIE_LIST = "movies_list";
    private String sortByPath;
    private URL parseUrl;
    private TextView errorMessageTV;
    private ProgressBar progressBar;

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
//        recyclerView.setAdapter(adapter);
        adapter = new MoviesAdapter(new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie position) {
                String movie_title = position.getTitle();
                Toast.makeText(MainActivity.this, movie_title +" Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(MainActivity.this);
//        recyclerView.setAdapter(new MoviesAdapter(moviesList, new MoviesAdapter().OnItemClickListener() {
//            @Override public void onItemClick(Movie item) {
//                Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
//            }
//        }));

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

//    @Override
//    public void onClick(Movie movie) {
//        Toast.makeText(MainActivity.this,"Item Clicked", Toast.LENGTH_SHORT);
//        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
//        intent.putExtra(KEY_PARCEL_INTENT, movie);
//        startActivity(intent);
//    }

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

//    @Override
//    public void onItemClick(int position) {
//        Toast.makeText(MainActivity.this,"Item Clicked", Toast.LENGTH_SHORT);
//
//    }


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

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
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

}