package com.example.andrey.popularmovies.model;

import java.io.Serializable;

public class Movie implements Serializable {
    private int id;
    private String title;
    private String posterPath;
    private String overview;
    private float voteAverage;

    public Movie(int id, String overview, String posterPath, String title, float voteAverage) {
        this.id = id;
        this.overview = overview;
        this.posterPath = posterPath;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    public int getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return id == movie.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
