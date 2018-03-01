package com.leafnext.kickbackmoviedatabase;


import android.content.ContentValues;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.leafnext.kickbackmoviedatabase.FetchMovieDetailsAsyncTask.OnTaskCompletedDetail;
import com.leafnext.kickbackmoviedatabase.Utils.NetworkUtils;
import com.leafnext.kickbackmoviedatabase.database.MovieDatabaseContract;
import com.leafnext.kickbackmoviedatabase.database.MovieDatabaseContract.MovieInfoContract;
import com.leafnext.kickbackmoviedatabase.database.MovieDatabaseHelper;
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
    private TrailerViewAdapter mTrailerViewAdapter;
    private ReviewViewAdapter mReviewViewAdapter;
    private SQLiteDatabase favouriteMovieDatabase;
    private  MovieInfo selectedMovieDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();

        selectedMovieDetails = intent.getParcelableExtra("movieDetails");

        MovieDatabaseHelper databaseHelper = new MovieDatabaseHelper(this);
        favouriteMovieDatabase = databaseHelper.getWritableDatabase();

        mTrailerViewAdapter = new TrailerViewAdapter(Movie_Details_Activity.this);
        mReviewViewAdapter = new ReviewViewAdapter(Movie_Details_Activity.this);

        //trailer recycler view stuff

        RecyclerView trailerRecyclerView = findViewById(R.id.trailerRecyclerView);

        trailerRecyclerView.setAdapter(mTrailerViewAdapter);

        LinearLayoutManager trailerViewmanager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        trailerRecyclerView.setLayoutManager(trailerViewmanager);

        DividerItemDecoration trailerItemDecoration= new DividerItemDecoration(trailerRecyclerView.getContext(),trailerViewmanager.getOrientation());

        trailerRecyclerView.addItemDecoration(trailerItemDecoration);


        //review recycler stuff

        RecyclerView reviewRecyclerView  = findViewById(R.id.reviewRecyclerView);

        reviewRecyclerView.setAdapter(mReviewViewAdapter);

        LinearLayoutManager reviewViewmanager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        reviewRecyclerView.setLayoutManager(reviewViewmanager);

        DividerItemDecoration reviewItemDecoration = new DividerItemDecoration(reviewRecyclerView.getContext(),reviewViewmanager.getOrientation());

        reviewRecyclerView.addItemDecoration(reviewItemDecoration);


        // other stuff

        TextView movieTitle = findViewById(R.id.movieOriginalTitle);

        ImageView moviePoster = findViewById(R.id.moviePoster);

        TextView movieSummary = findViewById(R.id.movieOverview);

        TextView movieRating = findViewById(R.id.movieUserRating);

        TextView movieReleaseDate = findViewById(R.id.movieReleaseDate);

        favoriteButton = findViewById(R.id.favouriteButton);

        favoriteButton.setText("Mark as"+"\n"+"Favourite");

        favoriteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                addMovieToFavouriteDatabase(selectedMovieDetails);

            }
        });


         movieLengthTextView = findViewById(R.id.movieLength);

        movieLengthTextView.setText("Loading");


        SimpleDateFormat format  = new SimpleDateFormat("yyyy");


        mBar = findViewById(R.id.progressBarDetail);

        String movieId = "";





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

        Boolean movieExist = ifMoveExistInDatabase(selectedMovieDetails.getOriginalTitle());


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
            mTrailerViewAdapter.setData(response.getTrailers());
        }

        if (response.getReviews().size() > 0){
            mReviewViewAdapter.setData(response.getReviews());
        }

        movieLengthTextView.setText(movieLength+"min");

        Log.i("DetailsActivity",movieLength + "\n" + movieTrailer + "\n"+ movieReviews);

    }

    private long addMovieToFavouriteDatabase(MovieInfo info){

        ContentValues cv = new ContentValues();
        cv.put(MovieDatabaseContract.MovieInfoContract.COLUMN_MOVIE_TITLE,info.getOriginalTitle());
        return favouriteMovieDatabase.insert(MovieDatabaseContract.MovieInfoContract.TABLE_NAME,null,cv);

    }

    private boolean ifMoveExistInDatabase(String movieTitle){
        boolean movieAdded = false;
        String[] args = {MovieDatabaseContract.MovieInfoContract.COLUMN_MOVIE_TITLE};
        Cursor cursor = favouriteMovieDatabase.query(MovieDatabaseContract.MovieInfoContract.TABLE_NAME, args,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                int index = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_TITLE);
                String title = cursor.getString(index);
                if (movieTitle.equals(title)){
                    movieAdded = true;
                    break;
                }
            }while (cursor.moveToNext());

        }

        return movieAdded;
    }
}
