package com.example.android.redcarpetmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final ArrayList<Movie> mMovieArrayList;
    private final MovieListRecyclerViewOnClickListener mMovieListRecyclerViewOnClickListener;
    private final Context context;

    public MovieAdapter(
            ArrayList<Movie> movieArrayList
            , MovieListRecyclerViewOnClickListener movieListRecyclerViewOnClickListener
            , Context context) {
        mMovieArrayList = movieArrayList;
        mMovieListRecyclerViewOnClickListener = movieListRecyclerViewOnClickListener;
        this.context = context;
    }

    // Creates ViewHolder when the RecyclerViews are laid out.
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int viewHolderResourceId = R.layout.single_movie_layout;
        View view = inflater.inflate(viewHolderResourceId, viewGroup, false);
        return new MovieViewHolder(view);
    }

    // Called by the RecyclerView to display the movie poster at the particular position.
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    // Number of movie posters to be displayed
    @Override
    public int getItemCount() {
        return mMovieArrayList.size();
    }

    // Receives onClick messages
    public interface MovieListRecyclerViewOnClickListener {
        void onListItemClicked(int position);
    }

    //View Holder class for the recycler view adapter
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView posterImageView;

        public MovieViewHolder(View view) {
            super(view);
            posterImageView = (ImageView) view.findViewById(R.id.card_view_poster);
            view.setOnClickListener(this);
        }

        // Called when ImageView(movie poster) is clicked
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mMovieListRecyclerViewOnClickListener.onListItemClicked(position);
        }

        // bind image URL to view position
        public void bind(int position) {
            String imageURL = mMovieArrayList.get(position).getPoster_path();

            Picasso.with(context).load(imageURL).placeholder(R.drawable.red_carpet)
                    .error(R.drawable.red_carpet).into(posterImageView);
        }
    }
}

