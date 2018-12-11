package com.example.android.example.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.example.Adapters.TrailerAdapter;
import com.example.android.example.Data.Trailer;
import com.example.android.example.R;
import com.example.android.example.utilities.NetworkUtilities;

import java.net.URL;
import java.util.ArrayList;

import static com.example.android.example.utilities.NetworkUtilities.Youtube_BaseURL;

public class TrailerActivity extends AppCompatActivity implements TrailerAdapter.TrailerClickListener {

    private RecyclerView trailer_rv;
    private ArrayList<Trailer> trailers;
    private TrailerAdapter trailerAdapter;
    private String movie_id;

    /**
     * @param movieKey
     */
    @Override
    public void onClick(String movieKey) {
        Uri youtubeUri = Uri.parse(Youtube_BaseURL + movieKey);
        Intent startYoutube = new Intent(Intent.ACTION_VIEW, youtubeUri);
        if (startYoutube.resolveActivity(getPackageManager()) != null) {
            startActivity(startYoutube);
        }
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        trailer_rv = (RecyclerView) findViewById(R.id.trailer_rv);


        trailers = new ArrayList<Trailer>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        trailer_rv.setLayoutManager(layoutManager);
        trailer_rv.setHasFixedSize(true);
        trailerAdapter = new TrailerAdapter(this, this);
        trailer_rv.setAdapter(trailerAdapter);

        Intent myIntent = getIntent();
        movie_id = myIntent.getStringExtra("movie_id");
        String title = myIntent.getStringExtra("title");
        final URL trailerUrl = NetworkUtilities.buildTrailerUrl(movie_id);


        new TrailerActivity.trailerQueryTask().execute(trailerUrl);
        setTitle(title);
    }


    private class trailerQueryTask extends AsyncTask<URL, Void, ArrayList<Trailer>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * @param params
         * @return
         */
        @Override
        protected ArrayList<Trailer> doInBackground(URL... params) {
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

        /**
         * @param trailerQueryResult
         */
        @Override
        protected void onPostExecute(ArrayList<Trailer> trailerQueryResult) {
            super.onPostExecute(trailerQueryResult);
            if (trailerQueryResult != null) {
                trailers = trailerQueryResult;
                if (trailers.size() == 0) {

                    Toast.makeText(getApplicationContext(), "No Trailers Found", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    trailerAdapter.setTrailers(trailerQueryResult);
                }
            }

        }

    }

}



