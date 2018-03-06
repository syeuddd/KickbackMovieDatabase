package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.leafnext.kickbackmoviedatabase.FetchMovieAsyncTask.OnTaskCompleted;
import com.leafnext.kickbackmoviedatabase.Utils.NetworkUtils;
import com.leafnext.kickbackmoviedatabase.database.MovieDatabaseContract;
import com.leafnext.kickbackmoviedatabase.database.MovieDatabaseContract.MovieInfoContract;
import com.leafnext.kickbackmoviedatabase.database.MovieDatabaseHelper;
import com.leafnext.kickbackmoviedatabase.model.MovieInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnTaskCompleted,LoaderManager.LoaderCallbacks<ArrayList<MovieInfo>>{


    private ProgressBar bar;
    private GridViewAdapter gridViewAdapter;
    private SQLiteDatabase mDb;
    private ArrayList<MovieInfo> movieList;
    private final String MOVIE_LIST_KEY = "movieListKey";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieDatabaseHelper db = new MovieDatabaseHelper(this);

        StaggeredGridLayoutManager manager;

        mDb = db.getWritableDatabase();

        gridViewAdapter  = new GridViewAdapter(MainActivity.this, mDb);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(gridViewAdapter);

        bar =  findViewById(R.id.progressBar);


        // show 3 columns in landscape

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            manager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        }else {
            manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        }

        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.setLayoutManager(manager);

       // recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));

        if (savedInstanceState !=null){

            movieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
            gridViewAdapter.setData(movieList);

        }else {
            fetchMovies(NetworkUtils.TOP_RATED_MOVIES);
        }



        Stetho.initializeWithDefaults(this);




    }


    @Override
    protected void onStart() {
        super.onStart();
        gridViewAdapter.notifyDataSetChanged();
    }

    private void fetchMovies(String sortType){

        URL url = NetworkUtils.buildUrl(sortType);

        if (isDevicedConnected()){
           new FetchMovieAsyncTask(this,bar).execute(url);
        }else {
            Toast.makeText(this, R.string.noInternetErrorMessage,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.popularMovies:
                gridViewAdapter.setData(null);
                fetchMovies(NetworkUtils.POPULAR_MOVIES);
                return true;

            case R.id.topMovies:
                gridViewAdapter.setData(null);
                fetchMovies(NetworkUtils.TOP_RATED_MOVIES);
                return true;

            case R.id.favouriteCollection:
                gridViewAdapter.setData(null);
                getSupportLoaderManager().initLoader(0,null,this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private Boolean isDevicedConnected(){

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onTaskCompleted(ArrayList<MovieInfo> response) {

        movieList = response;
        gridViewAdapter.setData(response);

    }

    @Override
    public Loader<ArrayList<MovieInfo>> onCreateLoader(int id, Bundle args) {
        return new FetchMoviesFromDatabase(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieInfo>> loader, ArrayList<MovieInfo> data) {

        movieList = data;
        gridViewAdapter.setData(data);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieInfo>> loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieList != null){
            outState.putParcelableArrayList(MOVIE_LIST_KEY,movieList);
        }

    }

    //    private ArrayList<MovieInfo> fetchFavouritesMoviesFromDatabase(){
//
//        Cursor cursor = getContentResolver().query(MovieInfoContract.CONTENT_URI,null,null,null,null,null);
//        ArrayList<MovieInfo> favouriteMovieList = new ArrayList<>();
//
//        if (cursor.moveToFirst()) {
//            for (int i = 0; i < cursor.getCount(); i++) {
//
//                int titleIndex = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_TITLE);
//                int thumbnailIndex = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_THUMBNAIL);
//                int posterIndex = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_POSTER);
//                int overviewIndex = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_OVERVIEW);
//                int userRatingIndex = cursor.getColumnIndexOrThrow(MovieInfoContract.COLUMN_MOVIE_RATING);
//                int releaseDateIndex = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_RELEASE_DATE);
//
//                String title = cursor.getString(titleIndex);
//                String thumbnail = cursor.getString(thumbnailIndex);
//                String poster = cursor.getString(posterIndex);
//                String overview = cursor.getString(overviewIndex);
//                String userRating = cursor.getString(userRatingIndex);
//                String releaseDate = cursor.getString(releaseDateIndex);
//
//                MovieInfo movieInfo = new MovieInfo();
//                movieInfo.setOriginalTitle(title);
//                movieInfo.setThumbnailImage(thumbnail);
//                movieInfo.setPosterImage(poster);
//                movieInfo.setOverview(overview);
//                movieInfo.setVoteAverage(userRating);
//                movieInfo.setReleaseDate(releaseDate);
//
//                favouriteMovieList.add(i,movieInfo);
//
//
//            }
//        }
//
//        return favouriteMovieList;
//    }


    // create another method to check for internet connection by pinging to google.com
}
