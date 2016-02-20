package layout;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.andrey.popularmovies.MovieDetailPageActivity;
import com.example.andrey.popularmovies.R;
import com.example.andrey.popularmovies.adapter.GridViewAdapter;
import com.example.andrey.popularmovies.dao.MovieDb;
import com.example.andrey.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMain extends Fragment {
    private static String LOG_TAG = FragmentMain.class.getSimpleName();
    private GridViewAdapter gridAdapter;
    private int currentPage = 1;
    private Movie selectedMovie;

    public FragmentMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid_view);
        gridAdapter = new GridViewAdapter(rootView.getContext(), R.layout.image_item_view, new ArrayList<Movie>());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDetailPageActivity.MovieDetailPageFragment frag = (MovieDetailPageActivity.MovieDetailPageFragment) getFragmentManager().findFragmentById(R.id.dp_movie_fragment_container);
                selectedMovie = (Movie) parent.getItemAtPosition(position);
                if (frag == null) {
                    Intent intent = new Intent(rootView.getContext(), MovieDetailPageActivity.class);
                    intent.putExtra("movie", selectedMovie);

                    startActivity(intent);
                } else {
                    frag.updateContent(selectedMovie);
                }
            }
        });


        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount - visibleItemCount) {
                    MovieListRequestOperation movieListRequestOperation = new MovieListRequestOperation();
                    movieListRequestOperation.execute(++currentPage);
                }
            }
        });

        MovieListRequestOperation movieListRequestOperation = new MovieListRequestOperation();
        movieListRequestOperation.execute(1);

        return rootView;
    }


    private class MovieListRequestOperation extends AsyncTask<Integer, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Integer... params) {
            int pageToRequest = 1;
            if (params[0] != null) {
                pageToRequest = params[0];
            }

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
                    float voteAverage = (float) movieObject.getDouble("vote_average");
                    Date releaseDate = Date.valueOf(movieObject.getString("release_date"));
                    Movie movie = new Movie(id, overView, posterPath, title, voteAverage, releaseDate);
                    movies.add(movie);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Log.v(LOG_TAG, movies.toString());
            gridAdapter.addAll(movies);
            gridAdapter.notifyDataSetChanged();

            if (selectedMovie == null) {
                MovieDetailPageActivity.MovieDetailPageFragment frag = (MovieDetailPageActivity.MovieDetailPageFragment) getFragmentManager().findFragmentById(R.id.dp_movie_fragment_container);
                if (frag != null) {
                    selectedMovie = movies.get(0);
                    frag.updateContent(selectedMovie);
                }
            }
        }
    }

}
