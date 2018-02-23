package com.leafnext.kickbackmoviedatabase;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.leafnext.kickbackmoviedatabase.model.MovieInfo;
import com.squareup.picasso.Picasso;

public class Movie_Details_Activity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView movieTitle = findViewById(R.id.movieOriginalTitle);

        ImageView moviePoster = findViewById(R.id.moviePoster);

        TextView movieSummary = findViewById(R.id.movieOverview);

        TextView movieRating = findViewById(R.id.movieUserRating);

        TextView movieReleaseDate = findViewById(R.id.movieReleaseDate);


        Intent intent = getIntent();

        MovieInfo selectedMovieDetails = intent.getParcelableExtra("movieDetails");

        if (selectedMovieDetails != null){

            movieTitle.setText(getString(R.string.movieTitle)+" - "+selectedMovieDetails.getOriginalTitle());

            movieReleaseDate.setText(getString(R.string.releaseDate)+" - "+selectedMovieDetails.getReleaseDate());

            String baseUrl = "http://image.tmdb.org/t/p/w780";

            Uri baseUri = Uri.parse(baseUrl);

            Uri uri = Uri.withAppendedPath(baseUri,selectedMovieDetails.getPosterImage());

            Picasso.with(this)
                    .load(uri)
                    .into(moviePoster);

            movieRating.setText(getString(R.string.voteAverage)+" - "+selectedMovieDetails.getVoteAverage());

            movieSummary.setText(getString(R.string.movieOverview)+" - "+selectedMovieDetails.getOverview());


        }



    }

}
