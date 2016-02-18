package com.example.andrey.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.andrey.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_page);

        Movie movie = (Movie)getIntent().getSerializableExtra("movie");

        TextView movieTitleComp = (TextView) findViewById(R.id.dp_movieTitle);
        movieTitleComp.setText(movie.getTitle());

        TextView movieDescriptionComp = (TextView) findViewById(R.id.dp_movieDescription);
        movieDescriptionComp.setText(movie.getOverview());

        RatingBar movieRatingComp = (RatingBar) findViewById(R.id.dp_movieRating);
        movieRatingComp.setRating(movie.getVoteAverage());

        ImageView movieImage = (ImageView)findViewById(R.id.dp_movieImage);
        Picasso
                .with(this)
                .load("http://image.tmdb.org/t/p/original/" + movie.getPosterPath())
                .fit()
                .into(movieImage);
    }

}
