package com.example.android.example;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

        private Context mContext;
        private List<Movie> albumList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            }
        }


        public MoviesAdapter(Context mContext, List<Movie> albumList) {
            this.mContext = mContext;
            this.albumList = albumList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_card, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Movie movie = albumList.get(position);

            // loading album cover using Glide library
            Glide.with(mContext).load(movie.getThumbnail()).into(holder.thumbnail);
        }


        @Override
        public int getItemCount() {
            return albumList.size();
        }

}
