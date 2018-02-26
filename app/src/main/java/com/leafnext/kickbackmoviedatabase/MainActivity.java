package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.leafnext.kickbackmoviedatabase.database.MovieDatabaseHelper;
import com.leafnext.kickbackmoviedatabase.model.MovieInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnTaskCompleted{


    private ProgressBar bar;
    private GridViewAdapter gridViewAdapter;
    private SQLiteDatabase mDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieDatabaseHelper db = new MovieDatabaseHelper(this);

        mDb = db.getWritableDatabase();

        gridViewAdapter  = new GridViewAdapter(MainActivity.this, mDb);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(gridViewAdapter);

        bar =  findViewById(R.id.progressBar);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.setLayoutManager(manager);

       // recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));

        fetchMovies(NetworkUtils.TOP_RATED_MOVIES);

        Stetho.initializeWithDefaults(this);


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

        gridViewAdapter.setData(response);

    }

    // create another method to check for internet connection by pinging to google.com
}
