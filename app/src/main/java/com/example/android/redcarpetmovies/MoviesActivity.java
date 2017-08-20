package com.example.android.redcarpetmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity
        implements MovieAdapter.MovieListRecyclerViewOnClickListener {

    private static final String LOG_TAG = MoviesActivity.class.getSimpleName();

    public static ArrayList<Movie> movieArrayList;
    private RecyclerView movieListRecyclerView;
    private ProgressBar progressBar;

    public static final String POSTER_KEY = "poster_path";
    public static final String IS_ADULT_KEY = "isAdult";
    public static final String SYNOPSIS_KEY = "overview";
    public static final String RELEASE_DATE_KEY = "releaseDate";
    public static final String TITLE_KEY = "title";
    public static final String POPULARITY_KEY = "popularity";
    public static final String VOTE_AVERAGE_KEY = "voteAverage";

    public final String MOST_POPULAR_STATE = "popularity";

    String finalURLMostPopular;
    String finalURLTopRated;
    boolean sortByPopularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        //URL string for Most Popular movies
        final String baseUrlPopularity = "https://api.themoviedb.org/3/movie/popular?api_key=";

        //URL string for API Key
        final String apiKey = getResources().getString(R.string.api_key);

        //URL string to identify for US based english language
        final String postApiKeyUrl = "&language=en-US&page=1";

        //URL string for Top Rated movies
        final String baseUrlTopRated = "https://api.themoviedb.org/3/movie/top_rated?api_key=";

        finalURLMostPopular = baseUrlPopularity.concat(apiKey.concat(postApiKeyUrl));
        finalURLTopRated = baseUrlTopRated.concat(apiKey.concat(postApiKeyUrl));
        movieArrayList = new ArrayList<>();

        movieListRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading_Data);
        MovieAdapter movieAdapter = new MovieAdapter(movieArrayList, this, this);

        // This line reads from the resource file according to the orientation and sets different
        // number of columns for landscape and portrait mode.
        final int numberOfColumns = getResources().getInteger(R.integer.columns_in_grid_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        movieListRecyclerView.setAdapter(movieAdapter);
        movieListRecyclerView.setLayoutManager(gridLayoutManager);

        if (savedInstanceState != null && isNetworkAvailable()) {
            boolean value = savedInstanceState.getBoolean(MOST_POPULAR_STATE);
            if (value) {
                new GetMovieListAsyncTask().execute(finalURLMostPopular);
                sortByPopularity = true;
            } else {
                new GetMovieListAsyncTask().execute(finalURLTopRated);
                sortByPopularity = false;
            }
        }
        //Query and sort by Most Popular
        else if (isNetworkAvailable()) {
            new GetMovieListAsyncTask().execute(finalURLMostPopular);
            sortByPopularity = true;
        } else {
            // Notify user of no internet connection
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, getString(R.string.no_internet_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onListItemClicked(int position) {

        String poster_path = movieArrayList.get(position).getPoster_path();
        boolean isAdult = movieArrayList.get(position).isAdult();
        String synopsis = movieArrayList.get(position).getOverview();
        String releaseDate = movieArrayList.get(position).getReleaseDate();
        String title = movieArrayList.get(position).getTitle();
        double popularity = movieArrayList.get(position).getPopularity();
        double voteAverage = movieArrayList.get(position).getVoteAverage();

        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(POSTER_KEY, poster_path);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(IS_ADULT_KEY, isAdult);
        intent.putExtra(SYNOPSIS_KEY, synopsis);
        intent.putExtra(RELEASE_DATE_KEY, releaseDate);
        intent.putExtra(POPULARITY_KEY, popularity);
        intent.putExtra(VOTE_AVERAGE_KEY, voteAverage);

        startActivity(intent);
    }

    //A helper method to check Internet Connection Status
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class GetMovieListAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            String url = params[0];
            HTTPHandler httpHandler = new HTTPHandler();
            String response = httpHandler.getHTTPResponse(url);
            String baseUrlPosterPath = "http://image.tmdb.org/t/p/w185/";
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray results = jsonObject.getJSONArray("results");
                    ArrayList<Movie> movieList = new ArrayList<>();

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject object = results.getJSONObject(i);
                        String returnedPosterPath = object.getString("poster_path");
                        String poster_path = baseUrlPosterPath.concat(returnedPosterPath);
                        boolean isAdult = object.getBoolean("adult");
                        String synopsis = object.getString("overview");
                        String releaseDate = object.getString("release_date");
                        String title = object.getString("title");
                        Double popularity = object.getDouble("popularity");
                        Double voteAverage = object.getDouble("vote_average");

                        movieList.add(new Movie(poster_path, isAdult, synopsis, releaseDate, title,
                                popularity, voteAverage));
                    }

                    return movieList;
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "JSON exception", e.fillInStackTrace());
                    return null;
                }
            } else {
                Log.e(LOG_TAG, getString(R.string.no_internet_connection));
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieArrayList) {
            MoviesActivity.movieArrayList = movieArrayList;

            if (movieArrayList == null) {
                // Display to user if no response to movie API
                Toast.makeText(getApplicationContext()
                        , getString(R.string.no_response),
                        Toast.LENGTH_LONG).show();
            } else {
                // Call method to display movie data to user
                MovieAdapter movieAdapter =
                        new MovieAdapter(MoviesActivity.movieArrayList,
                                MoviesActivity.this, getBaseContext());
                showData();
                movieListRecyclerView.setAdapter(movieAdapter);
                movieListRecyclerView.invalidate();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Get data based on user preference
        if (id == R.id.action_sort_popularity && isNetworkAvailable()) {
            new GetMovieListAsyncTask().execute(finalURLMostPopular);
            showProgressBar();
            sortByPopularity = true;
        } else if (id == R.id.action_sort_topRated && isNetworkAvailable()) {
            new GetMovieListAsyncTask().execute(finalURLTopRated);
            showProgressBar();
            sortByPopularity = false;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method called to hide progress spinner and
    // display RecyclerView (extracted data assigned to views)
    private void showData() {
        progressBar.setVisibility(View.GONE);
        movieListRecyclerView.setVisibility(View.VISIBLE);
    }

    // Method called to hide RecyclerView and display progress spinner
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        movieListRecyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(MOST_POPULAR_STATE, sortByPopularity);
    }
}