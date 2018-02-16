package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leafnext.kickbackmoviedatabase.model.MovieInfo;
import com.squareup.picasso.Picasso;

public class Movie_Details_Activity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView movieTitle = (TextView)findViewById(R.id.movieOriginalTitle);

        ImageView moviePoster = (ImageView)findViewById(R.id.moviePoster);

        TextView movieSummary = (TextView)findViewById(R.id.movieOverview);

        TextView movieRating = (TextView)findViewById(R.id.movieUserRating);

        TextView movieReleaseDate = (TextView) findViewById(R.id.movieReleaseDate);


        Intent intent = getIntent();

        MovieInfo selectedMovieDetails = (MovieInfo) intent.getParcelableExtra("movieDetails");

        movieTitle.setText(selectedMovieDetails.getOriginalTitle());


        // settings the image
        String baseUrl = "http://image.tmdb.org/t/p/w780";

        Uri baseUri = Uri.parse(baseUrl);

        Uri uri = Uri.withAppendedPath(baseUri,selectedMovieDetails.getPoster());

        Picasso.with(this)
                .load(uri)
                .into(moviePoster);

        movieSummary.setText(selectedMovieDetails.getOverview());

        movieRating.setText(selectedMovieDetails.getVoteAverage());

        movieReleaseDate.setText(selectedMovieDetails.getReleaseDate());

    }


}
