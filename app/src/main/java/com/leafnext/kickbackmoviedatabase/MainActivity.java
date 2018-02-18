package com.leafnext.kickbackmoviedatabase;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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


    ArrayList<MovieInfo> movieInfoArrayList;
    ProgressBar bar;
    RecyclerView recyclerView;
//    GridViewAdapter gridViewAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        movieInfoArrayList = new ArrayList<>();



        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        bar = (ProgressBar) findViewById(R.id.progressBar);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);


        recyclerView.setLayoutManager(staggeredGridLayoutManager);


       // fetchMovies(NetworkUtils.POPULAR_MOVIES);

    }

    public void fetchMovies(String sortType){

        URL url = NetworkUtils.buildUrl(sortType);

        new fetchData().execute(url);

    }




    public class fetchData extends AsyncTask<URL,Void,ArrayList<MovieInfo>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieInfo> doInBackground(URL... params) {

            String response = "";
            ArrayList<MovieInfo> movieInfos = new ArrayList<>();
            try {
                response = NetworkUtils.getResponseFromHttpUrl(params[0]);

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


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return movieInfos;

        }


        @Override
        protected void onPostExecute(ArrayList<MovieInfo> movieInfos) {
            super.onPostExecute(movieInfos);

            bar.setVisibility(View.INVISIBLE);

               GridViewAdapter gridViewAdapter = new GridViewAdapter(MainActivity.this,movieInfos);
                gridViewAdapter.notifyDataSetChanged();

                recyclerView.setAdapter(gridViewAdapter);

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

                //Toast.makeText(this,"PopularMovies",Toast.LENGTH_SHORT).show();
                fetchMovies(NetworkUtils.POPULAR_MOVIES);
                return true;
            case R.id.topMovies:
                Toast.makeText(this,"TopMovies",Toast.LENGTH_SHORT).show();
                //fetchMovies(NetworkUtils.TOP_RATED_MOVIES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
