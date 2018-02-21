package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.leafnext.kickbackmoviedatabase.FetchMovieAsyncTask.OnTaskCompleted;
import com.leafnext.kickbackmoviedatabase.Utils.NetworkUtils;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridViewAdapter  = new GridViewAdapter(MainActivity.this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(gridViewAdapter);

        bar =  findViewById(R.id.progressBar);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        }else {

            recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        }

        fetchMovies(NetworkUtils.TOP_RATED_MOVIES);

        isDevicedConnectedToInternet();

    }

    private void fetchMovies(String sortType){

        URL url = NetworkUtils.buildUrl(sortType);

        if (isDevicedConnected()){
            FetchMovieAsyncTask task = new FetchMovieAsyncTask(this,bar);
            task.execute(url);
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

    private boolean isDevicedConnectedToInternet(){

        try {
        Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            if (reachable){
                System.out.println("Internet access");
                return reachable;
            }else {
                System.out.println("No Internet access");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onTaskCompleted(ArrayList<MovieInfo> response) {
         gridViewAdapter.setData(response);
    }
}
