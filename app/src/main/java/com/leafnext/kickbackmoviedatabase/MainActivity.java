package com.leafnext.kickbackmoviedatabase;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
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
    GridViewAdapter gridViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        movieInfoArrayList = new ArrayList<>();



        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        bar = (ProgressBar) findViewById(R.id.progressBar);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);


        recyclerView.setLayoutManager(staggeredGridLayoutManager);


        URL url = NetworkUtils.buildUrl(NetworkUtils.POPULAR_MOVIES);
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

                    MovieInfo eachMovieInfo = new MovieInfo();

                    eachMovieInfo.setOriginalTitle(movieTitle);
                    eachMovieInfo.setPoster(moviePoster);
                    eachMovieInfo.setOverview(movieOverview);
                    eachMovieInfo.setReleaseDate(movieReleaseDate);
                    eachMovieInfo.setThumbnailImage(movieThumbnailImage);

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

            gridViewAdapter = new GridViewAdapter(MainActivity.this,movieInfos);

            recyclerView.setAdapter(gridViewAdapter);

        }
    }




}
