package com.example.andrey.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import com.example.andrey.popularmovies.adapter.GridViewAdapter;
import com.example.andrey.popularmovies.dao.MovieDb;
import com.example.andrey.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.movies_grid_view);
        gridAdapter = new GridViewAdapter(this, R.layout.image_item_view, new ArrayList<Movie>());
        gridView.setAdapter(gridAdapter);

        MovieListRequestOperation movieListRequestOperation = new MovieListRequestOperation();
        movieListRequestOperation.execute();
    }

    private class MovieListRequestOperation extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... params) {
            int pageToRequest = 1;
            JSONArray result = MovieDb.getNowPlayingMovies(pageToRequest);
            Log.v(LOG_TAG, result.toString());
            return result;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (jsonArray == null) {
                return;
            }

            // Convert JSONArray with movies to movie list
            List<Movie> movies = new ArrayList<>(20);
            // I intentionally do not use the GSON library here: https://github.com/google/gson
            // GSON ~300Kb library
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject movieObject = jsonArray.getJSONObject(i);
                    int id = movieObject.getInt("id");
                    String posterPath = movieObject.getString("poster_path");
                    String overView = movieObject.getString("overview");
                    String title = movieObject.getString("title");
                    double voteAverage = movieObject.getDouble("vote_average");
                    Movie movie = new Movie(id, overView, posterPath, title, voteAverage);
                    movies.add(movie);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Log.v(LOG_TAG, movies.toString());
            gridAdapter.clear();
            gridAdapter.addAll(movies);
            gridAdapter.notifyDataSetChanged();
        }
    }
}
