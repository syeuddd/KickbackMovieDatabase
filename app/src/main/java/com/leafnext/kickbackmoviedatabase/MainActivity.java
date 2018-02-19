package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
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
import com.leafnext.kickbackmoviedatabase.Utils.NetworkUtils;
import com.leafnext.kickbackmoviedatabase.model.MovieInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{


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

        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        fetchMovies(NetworkUtils.TOP_RATED_MOVIES);

    }

    private void fetchMovies(String sortType){

        URL url = NetworkUtils.buildUrl(sortType);

        if (isDevicedConnected()){
            new fetchData().execute(url);
        }else {
            Toast.makeText(this, R.string.noInternetErrorMessage,Toast.LENGTH_SHORT).show();
        }


    }


    private class fetchData extends AsyncTask<URL,Void,ArrayList<MovieInfo>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieInfo> doInBackground(URL... params) {

            ArrayList<MovieInfo> movieInfos = new ArrayList<>();
            try {


                String response = NetworkUtils.getResponseFromHttpUrl(params[0]);

                if (response != null){
                    JSONObject movieDatabaseResult = new JSONObject(response);
                    JSONArray resultsArray = movieDatabaseResult.getJSONArray("results");
                    for (int i=0; i<resultsArray.length(); i++){

                        JSONObject eachMovieJson = resultsArray.getJSONObject(i);

                        String movieTitle = eachMovieJson.getString("original_title");
                        String moviePoster = eachMovieJson.getString("poster_path");
                        String movieOverview = eachMovieJson.getString("overview");
                        String movieReleaseDate = eachMovieJson.getString("release_date");
                        String movieThumbnailImage = eachMovieJson.getString("backdrop_path");
                        String moveVoteAverage = eachMovieJson.getString("vote_average");

                        MovieInfo eachMovieInfo = new MovieInfo();

                        eachMovieInfo.setOriginalTitle(movieTitle);
                        eachMovieInfo.setPoster(moviePoster);
                        eachMovieInfo.setOverview(movieOverview);
                        eachMovieInfo.setReleaseDate(movieReleaseDate);
                        eachMovieInfo.setThumbnailImage(movieThumbnailImage);
                        eachMovieInfo.setVoteAverage(moveVoteAverage);

                        movieInfos.add(eachMovieInfo);
                    }
                }else {
                    return null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return movieInfos;

        }


        @Override
        protected void onPostExecute(ArrayList<MovieInfo> movieInfos) {

            if (movieInfos!= null) {
                super.onPostExecute(movieInfos);
                bar.setVisibility(View.INVISIBLE);
                gridViewAdapter.setData(movieInfos);
            }
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
}
