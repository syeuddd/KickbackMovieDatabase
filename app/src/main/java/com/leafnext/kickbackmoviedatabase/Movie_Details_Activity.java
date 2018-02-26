package com.leafnext.kickbackmoviedatabase;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.leafnext.kickbackmoviedatabase.FetchMovieDetailsAsyncTask.OnTaskCompletedDetail;
import com.leafnext.kickbackmoviedatabase.Utils.NetworkUtils;
import com.leafnext.kickbackmoviedatabase.model.MovieInfo;
import com.squareup.picasso.Picasso;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Movie_Details_Activity extends AppCompatActivity implements OnTaskCompletedDetail{

    ProgressBar mBar;
    TextView movieLengthTextView;
    Button favoriteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView movieTitle = findViewById(R.id.movieOriginalTitle);

        ImageView moviePoster = findViewById(R.id.moviePoster);

        TextView movieSummary = findViewById(R.id.movieOverview);

        TextView movieRating = findViewById(R.id.movieUserRating);

        TextView movieReleaseDate = findViewById(R.id.movieReleaseDate);

        favoriteButton = findViewById(R.id.favouriteButton);

        favoriteButton.setText("Mark as"+"\n"+"Favourite");


         movieLengthTextView = findViewById(R.id.movieLength);

        movieLengthTextView.setText("Loading");


        SimpleDateFormat format  = new SimpleDateFormat("yyyy");


        mBar = findViewById(R.id.progressBarDetail);

        String movieId = "";

        Intent intent = getIntent();

        MovieInfo selectedMovieDetails = intent.getParcelableExtra("movieDetails");

        if (selectedMovieDetails != null){

            movieId = selectedMovieDetails.getMovieId();

            movieTitle.setText(selectedMovieDetails.getOriginalTitle());

            try {
                Date year = format.parse(selectedMovieDetails.getReleaseDate());

                String releaseYear = format.format(year);

                movieReleaseDate.setText(releaseYear);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            String baseUrl = "http://image.tmdb.org/t/p/w780";

            Uri baseUri = Uri.parse(baseUrl);

            Uri uri = Uri.withAppendedPath(baseUri,selectedMovieDetails.getPosterImage());

            Picasso.with(this)
                    .load(uri)
                    .into(moviePoster);

            movieRating.setText(selectedMovieDetails.getVoteAverage() + "/10");

            movieSummary.setText(getString(R.string.movieOverview)+" - "+selectedMovieDetails.getOverview());


        }

        URL movieLengthUrl = NetworkUtils.getMoreMovieDetails(movieId,null);
        URL trailersUrl = NetworkUtils.getMoreMovieDetails(movieId,NetworkUtils.MOVIE_TRAILERS);
        URL reviewsUrl  = NetworkUtils.getMoreMovieDetails(movieId,NetworkUtils.MOVIE_REVIEWS);

        new FetchMovieDetailsAsyncTask(this,mBar,selectedMovieDetails).execute(movieLengthUrl,trailersUrl,reviewsUrl);

    }

    @Override
    public void onTaskCompletedDetailed(MovieInfo response) {

        String movieLength = "";
        String movieTrailer = "";
        String movieReviews = "";

        if (response.getMovieLength()!= null){
             movieLength = response.getMovieLength();
        }

        if(response.getTrailers().size() > 0){
            movieTrailer = response.getTrailers().get(0);
        }

        if (response.getReviews().size() > 0){
            movieReviews = response.getReviews().get(0);
        }

        movieLengthTextView.setText(movieLength+"min");

        Log.i("DetailsActivity",movieLength + "\n" + movieTrailer + "\n"+ movieReviews);

    }
}
