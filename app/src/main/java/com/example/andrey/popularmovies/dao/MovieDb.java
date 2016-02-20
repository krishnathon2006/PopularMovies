package com.example.andrey.popularmovies.dao;

import android.net.Uri;
import android.util.Log;

import com.example.andrey.popularmovies.model.SearchCategory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MovieDb {
    private static String LOG_TAG = MovieDb.class.getSimpleName();

    /**
     * Get the list of popular movies on The Movie Database. This list refreshes every day.
     *
     * @param page Minimum 1, maximum 1000.
     * @return JSONArray of popular movies
     */
    public static JSONArray getMostPopularMovies(int page) {
        return sendRequest(getUrlToApi(SearchCategory.popular, page));
    }

    /**
     * Get the list of movies playing that have been, or are being released this week. This list refreshes every day.
     *
     * @param page Minimum 1, maximum 1000
     * @return JSONArray of playing now movies.
     */
    public static JSONArray getNowPlayingMovies(int page) {
        return sendRequest(getUrlToApi(SearchCategory.now_playing, page));
    }

    /**
     * Get the list of top rated movies. By default, this list will only include movies that have 50 or more votes. This list refreshes every day.
     *
     * @param page Minimum 1, maximum 1000
     * @return JSONArray of top rated movies
     */
    public static JSONArray getTopRatedMovies(int page) {
        return sendRequest(getUrlToApi(SearchCategory.top_rated, page));
    }

    private static String getApiKey() {
        return "SECRET_KEY";
    }

    private static JSONArray sendRequest(Uri uri) {
        try {
            URL url = new URL(uri.toString());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("api_key", getApiKey());
            connection.connect();

            Log.d(LOG_TAG, "Request url: " + url);

            InputStream inputStream = connection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            return new JSONObject(buffer.toString()).getJSONArray("results");

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error happened: ", e);
        }

        return null;
    }

    private static Uri getUrlToApi(SearchCategory category, int page) {
        final String SCHEME = "https";
        final String MOVIE_DB_API_VERSION = "3";
        final String MOVIE_DB_CATALOG_FOR_MOVIES = "movie";
        final String AUTHORITY_MOVIE_DB_URL = "api.themoviedb.org";

        return new Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY_MOVIE_DB_URL)
                .appendPath(MOVIE_DB_API_VERSION)
                .appendPath(MOVIE_DB_CATALOG_FOR_MOVIES)
                .appendPath(category.toString())
                .appendQueryParameter("page", String.valueOf(page))
                .appendQueryParameter("api_key", getApiKey())
                .build();
    }
}
