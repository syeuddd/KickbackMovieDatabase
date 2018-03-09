package com.leafnext.kickbackmoviedatabase;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
    private Boolean movieExist = false;
    private String currentMovieId = "";
    private boolean dataCameFromDatabase = false;
    public final String CURRENT_MOVIE_KEY = "currentMovieObject";
    RelativeLayout parentLayout;
    RecyclerView reviewRecyclerView;
    RecyclerView trailerRecyclerView;
    View trailerDivider;
    View reviewViewDivider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();

        if (savedInstanceState!= null){
            selectedMovieDetails = savedInstanceState.getParcelable(CURRENT_MOVIE_KEY);
        }else {
            selectedMovieDetails = intent.getParcelableExtra("movieDetails");
        }

        MovieDatabaseHelper databaseHelper = new MovieDatabaseHelper(this);
        favouriteMovieDatabase = databaseHelper.getWritableDatabase();

        mTrailerViewAdapter = new TrailerViewAdapter(Movie_Details_Activity.this);
        mReviewViewAdapter = new ReviewViewAdapter(Movie_Details_Activity.this);

        //trailer recycler view stuff

        trailerRecyclerView = findViewById(R.id.trailerRecyclerView);

        trailerRecyclerView.setAdapter(mTrailerViewAdapter);

        LinearLayoutManager trailerViewmanager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        trailerRecyclerView.setLayoutManager(trailerViewmanager);

        DividerItemDecoration trailerItemDecoration= new DividerItemDecoration(trailerRecyclerView.getContext(),trailerViewmanager.getOrientation());

        trailerRecyclerView.addItemDecoration(trailerItemDecoration);


        //review recycler stuff

        reviewRecyclerView  = findViewById(R.id.reviewRecyclerView);

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

        // layout changes if movies are loaded from database

        parentLayout = findViewById(R.id.detailsActivityParentRelativeLayout);

        trailerDivider = findViewById(R.id.trailerViewDivider);
        reviewViewDivider = findViewById(R.id.reviewViewDivider);

        if (dataCameFromDatabase){

            parentLayout.removeView(trailerDivider);
            parentLayout.removeView(reviewViewDivider);

        }


        currentMovieId = ifMoveExistInFaouriteDatabase(selectedMovieDetails.getMovieId());

        if (currentMovieId.isEmpty()){
            favoriteButton.setText("Mark as"+"\n"+"Favourite");
            favoriteButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.buttonBackgroundColor));
        }else {
            favoriteButton.setText("Remove from"+"\n"+"Favourites");
            favoriteButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.buttonBackgroundColorDeleteFavourite));
        }



        favoriteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Movie_Details_Activity.this.currentMovieId.isEmpty()){

                  int rowsDeleted = removeMovieFromFavouriteDatabase(Movie_Details_Activity.this.currentMovieId);

                    if (rowsDeleted>0){
                        favoriteButton.setText("Mark as"+"\n"+"Favourite");
                        favoriteButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.buttonBackgroundColor));
                        Movie_Details_Activity.this.currentMovieId = "";
                    }

                }else {

                    String newMovieDatabaseId = addMovieToFavouriteDatabase(selectedMovieDetails);
                    if (!newMovieDatabaseId.isEmpty()) {
                        favoriteButton.setText("Remove from"+"\n"+"Favourites");
                        favoriteButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.buttonBackgroundColorDeleteFavourite));
                        Movie_Details_Activity.this.currentMovieId = newMovieDatabaseId;
                        Log.i("Details_Activity","Still stuck in removed from favorites block");
                    }

                }
            }
        });


        movieLengthTextView = findViewById(R.id.movieLength);

        movieLengthTextView.setText("Loading");


        SimpleDateFormat format  = new SimpleDateFormat("yyyy");


        mBar = findViewById(R.id.progressBarDetail);

        String movieId = "";


        if (selectedMovieDetails != null){

            if (selectedMovieDetails.getMovieId()!=null){
                movieId = selectedMovieDetails.getMovieId();
            }

            movieTitle.setText(selectedMovieDetails.getOriginalTitle());

            try {
                Date year = format.parse(selectedMovieDetails.getReleaseDate());

                String releaseYear = format.format(year);

                movieReleaseDate.setText(releaseYear);

            } catch (ParseException e) {
                e.printStackTrace();
            }


         String currentMoviePoster = selectedMovieDetails.getPosterImage();

            if (!currentMoviePoster.equals("null")){

                String baseUrl = "http://image.tmdb.org/t/p/w780";

                Uri baseUri = Uri.parse(baseUrl);

                Uri uri = Uri.withAppendedPath(baseUri,selectedMovieDetails.getPosterImage());

                Picasso.with(this)
                        .load(uri)
                        .into(moviePoster);
            }else {
                Picasso.with(this)
                        .load(R.drawable.ic_broken_image_black_24dp)
                        .into(moviePoster);
            }


            movieRating.setText(selectedMovieDetails.getVoteAverage() + "/10");

            movieSummary.setText(getString(R.string.movieOverview)+" - "+selectedMovieDetails.getOverview());


        }


        if ((!movieId.isEmpty()) && (!movieId.equals("null"))) {

            URL movieLengthUrl = NetworkUtils.getMoreMovieDetails(movieId, null);
            URL trailersUrl = NetworkUtils.getMoreMovieDetails(movieId, NetworkUtils.MOVIE_TRAILERS);
            URL reviewsUrl = NetworkUtils.getMoreMovieDetails(movieId, NetworkUtils.MOVIE_REVIEWS);

            new FetchMovieDetailsAsyncTask(this, mBar, selectedMovieDetails).execute(movieLengthUrl, trailersUrl, reviewsUrl);
        }

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
        }else {
            parentLayout.removeView(trailerDivider);
        }

        if (response.getReviews().size() > 0){
            mReviewViewAdapter.setData(response.getReviews());
        }else {
            parentLayout.removeView(reviewViewDivider);
        }

        movieLengthTextView.setText(movieLength+"min");

        Log.i("DetailsActivity",movieLength + "\n" + movieTrailer + "\n"+ movieReviews);

    }

    private String addMovieToFavouriteDatabase(MovieInfo info){

        ContentValues cv = new ContentValues();

            cv.put(MovieDatabaseContract.MovieInfoContract.COLUMN_MOVIE_TITLE,info.getOriginalTitle());
            cv.put(MovieInfoContract.COLUMN_MOVIE_ID, info.getMovieId());
            cv.put(MovieInfoContract.COLUMN_MOVIE_THUMBNAIL,info.getThumbnailImage());
            cv.put(MovieInfoContract.COLUMN_MOVIE_POSTER,info.getPosterImage());
            cv.put(MovieInfoContract.COLUMN_MOVIE_OVERVIEW, info.getOverview());
            cv.put(MovieInfoContract.COLUMN_MOVIE_RATING,info.getVoteAverage());
            cv.put(MovieInfoContract.COLUMN_MOVIE_RELEASE_DATE,info.getReleaseDate());

            Uri uri = getContentResolver().insert(MovieInfoContract.CONTENT_URI,cv);

           if (uri != null){

               String movieId = info.getMovieId();
               return movieId;
           }

        return "";
    }

    private String ifMoveExistInFaouriteDatabase(String movieId){

        String[] args = {MovieInfoContract.COLUMN_MOVIE_ID};

        Cursor cursor = getContentResolver().query(MovieInfoContract.CONTENT_URI,args,null,null,null,null);


         //= favouriteMovieDatabase.query(MovieDatabaseContract.MovieInfoContract.TABLE_NAME, args,null,null,null,null,null);

        if (cursor.moveToFirst()){
            do {
                int movieIdIndex = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_ID);

                String mMovieId = cursor.getString(movieIdIndex);

                if (movieId.equals(mMovieId)){
                    cursor.close();
                    return mMovieId;
                }
            }while (cursor.moveToNext());

        }
            cursor.close();
            return "";
    }

    private int removeMovieFromFavouriteDatabase(String currentMovieId) {

        String[] args = {MovieInfoContract.COLUMN_MOVIE_ID};

        String where = MovieInfoContract.COLUMN_MOVIE_ID+"="+currentMovieId;


        Uri deleteUri = Uri.withAppendedPath(MovieInfoContract.CONTENT_URI,currentMovieId);

        int rowsDeleted = getContentResolver().delete(deleteUri,where,null);

          //favouriteMovieDatabase.delete(MovieInfoContract.TABLE_NAME,MovieInfoContract._ID+"="+currentMovieId,null);

//        if (rowsDeleted>0){
//            Toast.makeText(this,"Movie removed from favourites",Toast.LENGTH_SHORT).show();
//        }

        return rowsDeleted;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (selectedMovieDetails!= null){
            outState.putParcelable(CURRENT_MOVIE_KEY,selectedMovieDetails);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return true;
    }
}
