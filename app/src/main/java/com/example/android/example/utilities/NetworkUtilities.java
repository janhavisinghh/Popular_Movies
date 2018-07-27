package com.example.android.example.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtilities {
    final static String POPMOV_BASE_URL =
            "https://api.themoviedb.org/3/movie/popular";

    final static String PARAM_API_KEY = "api_key";

    final static String PARAM_LANG = "language";
    final static String lang = "en-US";
    final static String api_key = "267b5b0e4de9ead6b9925df334cc7eba" ;


    public static URL buildUrl() {

        Uri builtUri = Uri.parse(POPMOV_BASE_URL).buildUpon()
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
}
