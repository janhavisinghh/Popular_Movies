package com.example.android.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailsActivity extends AppCompatActivity {
    private static final String KEY_PARCEL = "selected_movie";
    TextView title_tv;
    TextView synopsis_tv;
    TextView rating_tv;
    TextView date_tv;
    ImageView poster_iv;

    public String title;
    public String synopsis;
    public String rating;
    public String date;
    public String poster;

    private Movie movie;


    public static final String BASE_PATH = "http://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        title_tv = findViewById(R.id.title_tv_detail);
        synopsis_tv =findViewById(R.id.synopsis_tv_detail);
        rating_tv = findViewById(R.id.rating_tv_detail);
        date_tv =  findViewById(R.id.release_tv_detail);
        poster_iv =  findViewById(R.id.poster_iv_detail);

        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        synopsis = intent.getExtras().getString("overview");
        rating = intent.getExtras().getString("vote_average");
        date = intent.getExtras().getString("release_date");
        poster = intent.getExtras().getString("poster_path");

        title_tv.setText(title);
        synopsis_tv.setText(synopsis);
        rating_tv.setText(rating);
        date_tv.setText(date);


        if (poster.equals("null")) {
            poster_iv.setImageResource(R.drawable.no_image);
        } else {
            String posterUrl = BASE_PATH + poster;
            Picasso.get().load(posterUrl).fit().centerCrop().into(poster_iv);
        }
    }

}



