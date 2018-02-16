package com.leafnext.kickbackmoviedatabase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.leafnext.kickbackmoviedatabase.Utils.NetworkUtils;
import com.leafnext.kickbackmoviedatabase.model.MovieInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by syedehteshamuddin on 2018-02-14.
 */

public class GridViewFragment extends Fragment{

    ArrayList<MovieInfo> movieInfoArrayList;
    ProgressBar bar;
    RecyclerView recyclerView;
    GridViewAdapter gridViewAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.grid_view_fragment_layout,container,false);

        movieInfoArrayList = new ArrayList<>();

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);

        bar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);


        recyclerView.setLayoutManager(staggeredGridLayoutManager);


        URL url = NetworkUtils.buildUrl(NetworkUtils.POPULAR_MOVIES);

        new fetchData().execute(url);

        return rootView;

    }


    public class fetchData extends AsyncTask<URL,Void,ArrayList<MovieInfo>> {

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

            gridViewAdapter = new GridViewAdapter(getContext(),movieInfos);

            recyclerView.setAdapter(gridViewAdapter);

        }
    }

}
