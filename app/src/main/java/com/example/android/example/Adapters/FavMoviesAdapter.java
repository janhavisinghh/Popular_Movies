package com.example.android.example.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.example.Activity.DetailsActivity;
import com.example.android.example.Data.Movie;
import com.example.android.example.Database.MoviesContract;
import com.example.android.example.R;

import java.util.List;

public class FavMoviesAdapter extends RecyclerView.Adapter<FavMoviesAdapter.FavViewHolder> {

    public static final String BASE_PATH = "http://image.tmdb.org/t/p/w342";
    public List<Movie> movies;
    private Cursor mCursor;
    private Context mContext;

    /**
     * @param context
     * @param cursor
     */
    public FavMoviesAdapter(@NonNull Context context, Cursor cursor) {

        this.mContext = context;
        this.mCursor = cursor;
    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_card, parent, false);

        return new FavViewHolder(view);
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, final int position) {
        mCursor.moveToPosition(position);

        String TABLE_NAME = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE));
        final String COLUMN_TITLE = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE));
        final String COLUMN_POSTER_PATH = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH));
        final String COLUMN_OVERVIEW = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW));
        final String COLUMN_USER_RATING = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_USER_RATING));
        final String COLUMN_RELEASE_DATE = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE));
        final String COLUMN_MOVIE_ID = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID));
        String poster = BASE_PATH + COLUMN_POSTER_PATH;

        Glide.with(mContext)
                .load(poster)
                .centerCrop()
                .into(holder.imageView);
        holder.bottom_Header_tv.setText(COLUMN_TITLE);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("title", COLUMN_TITLE);
                intent.putExtra("poster_path", COLUMN_POSTER_PATH);
                intent.putExtra("vote_average", COLUMN_USER_RATING);
                intent.putExtra("release_date", COLUMN_RELEASE_DATE);
                intent.putExtra("overview", COLUMN_OVERVIEW);
                intent.putExtra("id", COLUMN_MOVIE_ID);

                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * @param newCursor
     */
    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    class FavViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        private TextView bottom_Header_tv;

        /**
         * @param itemView
         */
        public FavViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumbnail);
            bottom_Header_tv = itemView.findViewById(R.id.bottom_header_tv);

        }

    }

}
