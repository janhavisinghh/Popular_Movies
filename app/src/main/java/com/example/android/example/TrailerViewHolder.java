package com.example.android.example;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.example.Data.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.android.example.Adapters.TrailerAdapter.YT_BASE_PATH;

public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView shareIcon;
    ImageView trailerImageView;
    TextView videoTitleTextView;
    TextView videoTypeTextView;
    List<Trailer> trailer;
    Context context;
    private String youtube_thumbail = "https://img.youtube.com/vi/";

    /**
     * @param itemView
     * @param context
     * @param trailer
     */
    public TrailerViewHolder(View itemView, final Context context, final List<Trailer> trailer) {
        super(itemView);
        trailerImageView = (ImageView) itemView.findViewById(R.id.iv_detail_trailer);
        videoTitleTextView = itemView.findViewById(R.id.tv_video_title);
        videoTypeTextView = itemView.findViewById(R.id.tv_video_type);
        shareIcon = itemView.findViewById(R.id.share_icon);
        this.trailer = trailer;

        this.context = context;
        itemView.setOnClickListener(this);
    }

    /**
     * @param listIndex
     */
    public void bind(int listIndex) {
        String trailerThumbnailUrl = youtube_thumbail + trailer.get(listIndex).getTrailer_thumbnail() + "/maxresdefault.jpg";
        Picasso.get().load(trailerThumbnailUrl).placeholder(R.drawable.black_default).error(R.drawable.black_default).centerCrop().fit().into(trailerImageView);
        videoTitleTextView.setText(trailer.get(listIndex).getTrailer_name());
        videoTypeTextView.setText(trailer.get(listIndex).getTrailer_type());
    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        String youtube_url = YT_BASE_PATH + trailer.get(position).getTrailer_thumbnail();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube_url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.google.android.youtube");
        context.startActivity(intent);
    }
}

