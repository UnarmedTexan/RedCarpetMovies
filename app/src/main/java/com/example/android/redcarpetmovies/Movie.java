package com.example.android.redcarpetmovies;

public class Movie {

    private final String mPoster_path;
    private final boolean mIsAdult;
    private final String mSynopsis;
    private final String mReleaseDate;
    private final String mTitle;
    private final double mPopularity;
    private final double mVoteAverage;

    public Movie(String poster_path , boolean isAdult , String overview , String releaseDate,
                 String title , double popularity, double voteAverage){
        mPoster_path = poster_path;
        mIsAdult = isAdult;
        mSynopsis = overview;
        mReleaseDate = releaseDate;
        mTitle = title;
        mPopularity = popularity;
        mVoteAverage = voteAverage;
    }

    public String getPoster_path() {
        return mPoster_path;
    }

    public boolean isAdult() {
        return mIsAdult;
    }

    public String getOverview() {
        return mSynopsis;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }
}