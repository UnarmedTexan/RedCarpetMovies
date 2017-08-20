package com.example.android.redcarpetmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class MovieDetailsActivity extends AppCompatActivity {

    // Initialize data and views to be displayed in activity_movie_details views
    private String poster_path;
    private String synopsis;
    private String releaseDate;
    private String title;
    private double voteAverage;

    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView voteAverageTotalTextView;
    private TextView releaseDateTextView;
    private TextView synopsisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set Image View used to display movie poster in activity_movie_details
        posterImageView = (ImageView) findViewById(R.id.details_poster);

        //Set Text View used to display movie title in activity_movie_details
        titleTextView = (TextView) findViewById(R.id.details_movie_title);

        //Set Text View used to display vote avg for movie in activity_movie_details
        voteAverageTotalTextView = (TextView) findViewById(R.id.details_vote_average);

        //Set Text View used to display movie release date in activity_movie_details
        releaseDateTextView = (TextView) findViewById(R.id.details_release_date);

        //Set Text View used to display movie synopsis in activity_movie_details
        synopsisTextView = (TextView) findViewById(R.id.details_synopsis_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            poster_path = bundle.getString(MoviesActivity.POSTER_KEY);
            synopsis = bundle.getString(MoviesActivity.SYNOPSIS_KEY);
            releaseDate = bundle.getString(MoviesActivity.RELEASE_DATE_KEY);

            try {
                releaseDate = releaseDate.substring(0, 4);
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.e(MovieDetailsActivity.class.getName(), getString(R.string.no_date));
            }

            title = bundle.getString(MoviesActivity.TITLE_KEY);
            voteAverage = bundle.getDouble(MoviesActivity.VOTE_AVERAGE_KEY);

            // Set extracted Json information into activity_movie_details views
            try {
                // Movie poster
                Picasso.with(getBaseContext()).load(poster_path).into(posterImageView);

                // Movie title
                titleTextView.setText(title);

                // User rating
                String voteAverageTotalFinalText = voteAverage + "/10";
                voteAverageTotalTextView.setText(voteAverageTotalFinalText);

                // Movie release date
                releaseDateTextView.setText(releaseDate);

                // Movie synopsis
                synopsisTextView.setText(synopsis);

                // If no extracted Json information is available
            } catch (Exception e) {
                e.printStackTrace();
                // Notify user of no internet connection
                Toast.makeText(this, getString(R.string.no_internet_connection)
                        , Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

