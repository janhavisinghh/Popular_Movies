package com.example.android.example.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private String reviewer_name;
    private String review;

    public Review() {
        review = reviewer_name = null;
    }

    public Review(String reviewer_name, String review) {
        this.reviewer_name = reviewer_name;
        this.review = review;
    }

    public String getReviewer_name() {
        return reviewer_name;
    }

    public String getReview() {
        return review;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewer_name);
        dest.writeString(review);

    }

}
