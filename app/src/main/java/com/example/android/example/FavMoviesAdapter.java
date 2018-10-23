package com.example.android.example;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavMoviesAdapter extends RecyclerView.Adapter<FavMoviesAdapter.FavViewHolder> {

    private Cursor mCursor;
    public List<Movie> movies;
    private Context mContext;

    public static final String BASE_PATH = "http://image.tmdb.org/t/p/w342";


    public FavMoviesAdapter(Context context, Cursor cursor) {

        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_card, parent, false);

        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(position))
            return;
        String TABLE_NAME = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE));
        String COLUMN_TITLE = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE));
        String COLUMN_POSTER_PATH = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH));
        String COLUMN_OVERVIEW = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW));
        String COLUMN_USER_RATING = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_USER_RATING));
        String COLUMN_RELEASE_DATE = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE));
        String COLUMN_MOVIE_ID = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID));
        String poster = BASE_PATH + COLUMN_POSTER_PATH;
        Movie movies = new Movie(COLUMN_TITLE,  COLUMN_POSTER_PATH,  COLUMN_OVERVIEW,  COLUMN_USER_RATING,
                 COLUMN_RELEASE_DATE,  COLUMN_MOVIE_ID);

        Glide.with(mContext)
                .load(poster)
                .centerCrop()
                .into(holder.imageView);

//        holder.bind(position,movies, listener);


    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    class FavViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;

        public FavViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumbnail);
        }
//        public void bind(final int item, final Movie moviesitem, final OnItemClickListener listener) {
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onItemClick(moviesitem);
//                    Intent intent = new Intent(mContext, DetailsActivity.class);
//                    intent.putExtra("title", moviesitem.getTitle());
//                    intent.putExtra("poster_path", moviesitem.getPoster());
//                    intent.putExtra("vote_average", moviesitem.getUserRating());
//                    intent.putExtra("release_date", moviesitem.getReleaseDate());
//                    intent.putExtra("overview", moviesitem.getOverview());
//                    intent.putExtra("id", moviesitem.getmovieID());
//
//                    mContext.startActivity(intent);
//
//                }
//            });
//        }





    }

}
