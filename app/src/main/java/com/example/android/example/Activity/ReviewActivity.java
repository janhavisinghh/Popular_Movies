package com.example.android.example.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.example.Adapters.ReviewAdapter;
import com.example.android.example.Data.Review;
import com.example.android.example.R;
import com.example.android.example.utilities.NetworkUtilities;

import java.net.URL;
import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    public ArrayList<Review> reviews;
    private ReviewAdapter reviewAdapter;
    private RecyclerView review_rv;
    private String movie_id;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        review_rv = (RecyclerView) findViewById(R.id.review_rv);


        reviews = new ArrayList<Review>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        review_rv.setLayoutManager(layoutManager);
        review_rv.setHasFixedSize(true);
        reviewAdapter = new ReviewAdapter();
        review_rv.setAdapter(reviewAdapter);

        Intent myIntent = getIntent();
        movie_id = myIntent.getStringExtra("movie_id");
        String title = myIntent.getStringExtra("title");
        final URL reviewUrl = NetworkUtilities.buildReviewURL(movie_id);


        new reviewsQueryTask().execute(reviewUrl);
        setTitle(title);


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

    private void showReviewData() {
        review_rv.setVisibility(View.VISIBLE);
    }

    public class reviewsQueryTask extends AsyncTask<URL, Void, ArrayList<Review>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * @param params
         * @return
         */
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

        /**
         * @param ReviewQueryResult
         */
        @Override
        protected void onPostExecute(final ArrayList<Review> ReviewQueryResult) {
            if (ReviewQueryResult != null) {
                reviews = ReviewQueryResult;
                if (reviews.size() == 0) {

                    Toast.makeText(getApplicationContext(), "No Reviews Found", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    reviewAdapter.setReviews(ReviewQueryResult);
                    showReviewData();
                }
            }

        }

    }
}
