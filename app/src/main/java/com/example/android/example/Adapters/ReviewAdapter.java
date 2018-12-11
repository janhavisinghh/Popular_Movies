package com.example.android.example.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.example.Data.Review;
import com.example.android.example.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    public List<Review> reviews;
    private Context context;
    private TextView reviewer_name;
    private TextView review_by_reviewer;

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_item, parent, false);

        return new ReviewAdapter.ReviewViewHolder(view);
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ReviewAdapter.ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (reviews == null) {
            return 0;
        }
        return reviews.size();
    }

    /**
     * @param reviews
     */
    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        /**
         * @param itemView
         */
        public ReviewViewHolder(View itemView) {
            super(itemView);
            review_by_reviewer = itemView.findViewById(R.id.review);
            reviewer_name = itemView.findViewById(R.id.reviewer_name);


        }

        /**
         * @param item
         */
        public void bind(final int item) {
            review_by_reviewer.setText(reviews.get(item).getReview());
            reviewer_name.setText(reviews.get(item).getReviewer_name());

        }


    }
}
