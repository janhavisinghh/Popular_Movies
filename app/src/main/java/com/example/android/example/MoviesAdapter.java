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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    public static final String BASE_PATH = "http://image.tmdb.org/t/p/w342";
    public List<Movie> movies;
    public ImageView thumbnail;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
            }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

//    private GridItemListener listener;


//    public MoviesAdapter(GridItemListener listener) {
//        this.listener = listener;
//    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_card, parent, false);

        return new MovieViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        holder.bind(position);
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


//    public interface GridItemListener {
//        void onClick(Movie movie);
//    }


public class MovieViewHolder extends RecyclerView.ViewHolder  {

    final ImageView imageView;

    public MovieViewHolder(View view, final OnItemClickListener listener) {
        super(view);
        imageView = view.findViewById(R.id.thumbnail);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION)
                        listener.onItemClick(position);
                }
            }
        });
//        view.setOnClickListener(this);
    }

    /**
     * Set movie title and image
     *
     * @param itemIndex position of item
     */
    public void bind(int itemIndex) {
        String posterPath = movies.get(itemIndex).getPoster();

        if (posterPath.equals("null")) {
            Picasso.get().load(R.drawable.no_image).fit().centerCrop().into(imageView);
        } else {
            String posterUrl = BASE_PATH + posterPath;
            Picasso.get().load(posterUrl).fit().centerCrop().into(imageView);
        }
    }
//
//    @Override
//    public void onClick(View v) {
//        int clickedPosition = getAdapterPosition();
//        listener.onClick(movies.get(clickedPosition));
//    }
}
}
