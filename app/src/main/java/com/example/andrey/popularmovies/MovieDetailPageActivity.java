package com.example.andrey.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrey.popularmovies.model.Movie;
import com.example.andrey.popularmovies.util.Constants;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class MovieDetailPageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_page);

        MovieDetailPageFragment movieDetailPageFragment = (MovieDetailPageFragment) getSupportFragmentManager().findFragmentById(R.id.dp_movieContainer);
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        movieDetailPageFragment.updateContent(movie);
    }

    public static class MovieDetailPageFragment extends Fragment {
        private Movie movie;

        public MovieDetailPageFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                movie = (Movie) savedInstanceState.getSerializable("movie");
            }

            return inflater.inflate(R.layout.fragment_movie_detail_page, container, false);
        }

        @Override
        public void onStart() {
            super.onStart();
            updateValuesInComponents(movie);
        }

        public void updateContent(Movie movie) {
            this.movie = movie;
            updateValuesInComponents(movie);
        }

        @Override
        public void onSaveInstanceState(Bundle saveInstantState) {
            saveInstantState.putSerializable("movie", movie);
        }

        private void updateValuesInComponents(Movie movie) {
            if (movie != null && getView() != null) {
                TextView movieTitleComp = (TextView) getActivity().findViewById(R.id.dp_movieTitle);
                movieTitleComp.setText(movie.getTitle());

                TextView movieDescriptionComp = (TextView) getActivity().findViewById(R.id.dp_movieDescription);
                movieDescriptionComp.setText(movie.getOverview());

                TextView movieRatingComp = (TextView) getActivity().findViewById(R.id.dp_movieRating);
                movieRatingComp.setText(String.format("%.1f/10.0", movie.getVoteAverage()));

                TextView releaseDateComp = (TextView) getActivity().findViewById(R.id.dp_releaseDate);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                releaseDateComp.setText(dateFormat.format(movie.getReleaseDate()));

                ImageView movieImage = (ImageView) getActivity().findViewById(R.id.dp_movieImage);
                Picasso
                        .with(getActivity())
                        .load(Constants.urlToMoviePoster + movie.getPosterPath())
                        .placeholder(R.drawable.movie_placeholder)
                        .fit()
                        .into(movieImage);
            }
        }
    }
}
