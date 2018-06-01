package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.leafnext.kickbackmoviedatabase.database.MovieDatabaseHelper;
import com.leafnext.kickbackmoviedatabase.model.MovieInfo;

import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnTaskCompleted,LoaderManager.LoaderCallbacks<ArrayList<MovieInfo>>{


    private ProgressBar bar;
    private GridViewAdapter gridViewAdapter;
    private SQLiteDatabase mDb;
    private ArrayList<MovieInfo> movieListFromApi;
    private final String MOVIE_LIST_KEY = "movieListKey";
    private boolean currentViewisDatabase = false;



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


        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            manager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        }else {
            manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        }

        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.setLayoutManager(manager);

        if(savedInstanceState != null) {

            currentViewisDatabase = savedInstanceState.getBoolean("database");

            if (!currentViewisDatabase){

                movieListFromApi = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
                gridViewAdapter.setData(movieListFromApi);

            }
        }else {
            fetchMovies(NetworkUtils.TOP_RATED_MOVIES);
        }

        Stetho.initializeWithDefaults(this);

    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentViewisDatabase) {

            getSupportLoaderManager().restartLoader(0,null,this);

        }
        gridViewAdapter.notifyDataSetChanged();
    }

    private void fetchMovies(String sortType){

        URL url = NetworkUtils.buildUrl(sortType);

        if (isDevicedConnected()){
           new FetchMovieAsyncTask(this,bar).execute(url);
        }else {
            gridViewAdapter.setData(null);
            getSupportLoaderManager().initLoader(0,null,this);
            currentViewisDatabase = true;
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
                currentViewisDatabase =false;
                return true;

            case R.id.topMovies:
                gridViewAdapter.setData(null);
                fetchMovies(NetworkUtils.TOP_RATED_MOVIES);
                currentViewisDatabase = false;
                return true;

            case R.id.favouriteCollection:
                gridViewAdapter.setData(null);
                getSupportLoaderManager().initLoader(0,null,this);
                currentViewisDatabase = true;
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean isDevicedConnected(){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {

            View view = findViewById(R.id.mainLayout);
            Snackbar.make(view, R.string.no_internet_msg,Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    public void onTaskCompleted(ArrayList<MovieInfo> response) {

        movieListFromApi = response;
        gridViewAdapter.setData(response);

    }

    @Override
    public Loader<ArrayList<MovieInfo>> onCreateLoader(int id, Bundle args) {
        return new FetchMoviesFromDatabase(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieInfo>> loader, ArrayList<MovieInfo> data) {

        if (data!= null){
            gridViewAdapter.setData(data);
            stopLoader(0);

        }else {
            Toast.makeText(MainActivity.this, R.string.no_movies_stored_msg,Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieInfo>> loader) {

    }

    void stopLoader(int id){
        getSupportLoaderManager().destroyLoader(id);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("database",currentViewisDatabase);

        if (movieListFromApi != null){
            outState.putParcelableArrayList(MOVIE_LIST_KEY, movieListFromApi);
        }

    }

}
