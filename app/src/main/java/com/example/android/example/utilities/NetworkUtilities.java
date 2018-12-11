package com.example.android.example.utilities;

import android.net.Uri;

import com.example.android.example.Data.Movie;
import com.example.android.example.Data.Review;
import com.example.android.example.Data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtilities {
    public static final String Youtube_BaseURL =
            "https://www.youtube.com/watch";
    final static String POPMOV_BASE_URL =
            "https://api.themoviedb.org/3/movie";
    final static String PARAM_API_KEY = "api_key";

    final static String PARAM_LANG = "language";
    final static String lang = "en-US";
    final static String api_key = "267b5b0e4de9ead6b9925df334cc7eba";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_ID = "key";
    private static final String Trailer_TITLE = "name";
    private static final String Trailer_TYPE = "type";

    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_USER_RATING = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_MOVIEID = "id";
    private static final String SORT_BY_PARAM = "popular";
    private static final String video = "videos";
    private static final String review = "reviews";


    /**
     * @return
     */
    public static URL buildUrl() {

        Uri builtUri = Uri.parse(POPMOV_BASE_URL).buildUpon()
                .appendPath(SORT_BY_PARAM)
                .appendQueryParameter(PARAM_API_KEY, api_key)
                .appendQueryParameter(PARAM_LANG, lang)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * @param MOVIE_ID
     * @return
     */
    public static URL buildTrailerUrl(String MOVIE_ID) {

        Uri builtUri = Uri.parse(POPMOV_BASE_URL).buildUpon()
                .appendPath(MOVIE_ID)
                .appendPath(video)
                .appendQueryParameter(PARAM_API_KEY, api_key)
                .appendQueryParameter(PARAM_LANG, lang)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @param MOVIE_ID
     * @return
     */
    public static URL buildReviewURL(String MOVIE_ID) {

        Uri builtUri = Uri.parse(POPMOV_BASE_URL).buildUpon()
                .appendPath(MOVIE_ID)
                .appendPath(review)
                .appendQueryParameter(PARAM_API_KEY, api_key)
                .appendQueryParameter(PARAM_LANG, lang)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * @param SORT_BY_PARAM
     * @return
     */
    public static URL buildUrl(String SORT_BY_PARAM) {

        Uri builtUri = Uri.parse(POPMOV_BASE_URL).buildUpon()
                .appendPath(SORT_BY_PARAM)
                .appendQueryParameter(PARAM_API_KEY, api_key)
                .appendQueryParameter(PARAM_LANG, lang)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    /**
     * @param url
     * @return
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * @param json
     * @return
     * @throws JSONException
     */
    public static ArrayList<Movie> parseMovieJson(String json) throws JSONException {

        JSONObject root = new JSONObject(json);
        JSONArray results = root.getJSONArray(KEY_RESULTS);

        ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String title = result.getString(KEY_TITLE);
            String poster = result.getString(KEY_POSTER_PATH);
            String overview = result.getString(KEY_OVERVIEW);
            String userRating = result.getString(KEY_USER_RATING);
            String releaseDate = result.getString(KEY_RELEASE_DATE);
            String movieID = result.getString(KEY_MOVIEID);
            movies.add(new Movie(title, poster, overview, userRating, releaseDate, movieID));
        }
        return movies;
    }

    /**
     * @param json
     * @return
     * @throws JSONException
     */
    public static ArrayList<Trailer> parseTrailerJSON(String json) throws JSONException {

        JSONObject root = new JSONObject(json);
        JSONArray results = root.getJSONArray(KEY_RESULTS);

        ArrayList<Trailer> trailers = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String youtube_id = result.getString(KEY_ID);
            String trailer_name = result.getString(Trailer_TITLE);
            String trailer_type = result.getString(Trailer_TYPE);
            trailers.add(new Trailer(trailer_name, youtube_id, trailer_type));
        }
        return trailers;
    }

    /**
     * @param json
     * @return
     * @throws JSONException
     */
    public static ArrayList<Review> parseReviewJson(String json) throws JSONException {

        JSONObject root = new JSONObject(json);
        JSONArray results = root.getJSONArray(KEY_RESULTS);

        ArrayList<Review> reviews = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String author = result.getString(KEY_AUTHOR);
            String content = result.getString(KEY_CONTENT);
            reviews.add(new Review(author, content));
        }
        return reviews;
    }

    /**
     * @return
     */
    public static URL getDefaultSortByPathUrl() {
        return buildUrl();
    }

    /**
     * @param SORT_BY_PARAM
     * @return
     */
    public static URL getSortByPathUrl(String SORT_BY_PARAM) {
        return buildUrl(SORT_BY_PARAM);
    }

    /**
     * @return
     */
    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
