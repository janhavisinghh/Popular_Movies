package com.example.android.example.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

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
    private String trailer_name;
    private String trailer_thumbnail;
    private String trailer_type;

    public Trailer() {
        trailer_thumbnail = trailer_name = trailer_type = null;
    }

    public Trailer(String trailer_name, String trailer_thumbnail, String trailer_type) {
        this.trailer_name = trailer_name;
        this.trailer_thumbnail = trailer_thumbnail;
        this.trailer_type = trailer_type;
    }

    public String getTrailer_name() {
        return trailer_name;
    }

    public String getTrailer_thumbnail() {
        return trailer_thumbnail;
    }

    public String getTrailer_type() {
        return trailer_type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailer_name);
        dest.writeString(trailer_thumbnail);

    }

}