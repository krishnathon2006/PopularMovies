package com.example.andrey.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.andrey.popularmovies.R;
import com.example.andrey.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridViewAdapter extends ArrayAdapter<Movie> {
    private Context context;
    private int layoutResourceId;
    private List<Movie> movieList;

    public GridViewAdapter(Context context, int layoutResourceId, List<Movie> movieList) {
        super(context, layoutResourceId, movieList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.movieList = movieList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        Picasso
                .with(context).setIndicatorsEnabled(true);

        Picasso
                .with(context)
                .load("http://image.tmdb.org/t/p/original/" + movieList.get(position).getPosterPath())
                .placeholder(R.drawable.movie_placeholder).fit()
                .into((ImageView) convertView.findViewById(R.id.image_item_view));

        return convertView;
    }
}
