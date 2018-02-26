package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract.CommonDataKinds.Contactables;
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

/**
 * Created by syedehteshamuddin on 2018-02-20.
 */

public class FetchMovieAsyncTask extends AsyncTask <URL,Void,ArrayList<MovieInfo>>{

    private OnTaskCompleted taskCompleted;
    private ProgressBar mbar;

    public FetchMovieAsyncTask(OnTaskCompleted task, ProgressBar bar){
        taskCompleted = task;
        mbar = bar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mbar.setVisibility(View.VISIBLE);
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

                    String movieId = eachMovieJson.getString("id");
                    String movieTitle = eachMovieJson.getString("original_title");
                    String moviePoster = eachMovieJson.getString("poster_path");
                    String movieOverview = eachMovieJson.getString("overview");
                    String movieReleaseDate = eachMovieJson.getString("release_date");
                    String movieThumbnailImage = eachMovieJson.getString("backdrop_path");
                    String moveVoteAverage = eachMovieJson.getString("vote_average");


                    MovieInfo eachMovieInfo = new MovieInfo();

                    eachMovieInfo.setOriginalTitle(movieTitle);
                    eachMovieInfo.setPosterImage(moviePoster);
                    eachMovieInfo.setOverview(movieOverview);
                    eachMovieInfo.setReleaseDate(movieReleaseDate);
                    eachMovieInfo.setThumbnailImage(movieThumbnailImage);
                    eachMovieInfo.setVoteAverage(moveVoteAverage);
                    eachMovieInfo.setMovieId(movieId);

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

        mbar.setVisibility(View.INVISIBLE);
        taskCompleted.onTaskCompleted(movieInfos);

    }

    public interface OnTaskCompleted{
       void onTaskCompleted(ArrayList<MovieInfo> response);
   }

}
