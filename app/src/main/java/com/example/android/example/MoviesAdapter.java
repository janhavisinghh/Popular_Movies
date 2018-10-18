package com.example.android.example;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    public static final String BASE_PATH = "http://image.tmdb.org/t/p/w342";
    public List<Movie> movies;
    public List<Review> reviews;
    public ImageView thumbnail;
    private Context context;
    private int mCount;

    private final OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Movie position);
            }

    public MoviesAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_card, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        holder.bind(position,movies.get(position), listener);
    }

    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        }return movies.size();
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

public class MovieViewHolder extends RecyclerView.ViewHolder  {

    final ImageView imageView;

    public MovieViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.thumbnail);
        }

    public void bind(final int item, final Movie moviesitem, final OnItemClickListener listener) {
        final String posterPath = movies.get(item).getPoster();
        if (posterPath.equals("null")) {
            Picasso.get().load(R.drawable.no_image).fit().centerCrop().into(imageView);
        } else {
            String posterUrl = BASE_PATH + posterPath;
            Picasso.get().load(posterUrl).fit().centerCrop().into(imageView);

        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(moviesitem);
                Intent intent = new Intent(context,DetailsActivity.class);
                intent.putExtra("title", movies.get(item).getTitle());
                intent.putExtra("poster_path", movies.get(item).getPoster());
                intent.putExtra("vote_average", movies.get(item).getUserRating());
                intent.putExtra("release_date", movies.get(item).getReleaseDate());
                intent.putExtra("overview", movies.get(item).getOverview());
                intent.putExtra("id", movies.get(item).getmovieID());

                context.startActivity(intent);

            }
        });
    }
}
}
