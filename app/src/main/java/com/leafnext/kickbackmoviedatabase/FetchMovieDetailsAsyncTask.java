package com.leafnext.kickbackmoviedatabase;

import android.os.AsyncTask;
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

public class FetchMovieDetailsAsyncTask extends AsyncTask <URL,Void,MovieInfo>{

    private OnTaskCompletedDetail taskCompleted;
    private ProgressBar mbar;
    private MovieInfo currentMovieObject;

    public FetchMovieDetailsAsyncTask(OnTaskCompletedDetail task, ProgressBar bar, MovieInfo info){
        taskCompleted = task;
        mbar = bar;
        currentMovieObject = info;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected MovieInfo doInBackground(URL... params) {
        ArrayList<MovieInfo> movieInfos = new ArrayList<>();
        try {


            String movieLengthResponse = NetworkUtils.getResponseFromHttpUrl(params[0]);
            String movieTrailerResponse = NetworkUtils.getResponseFromHttpUrl(params[1]);
            String movieReviewsResponse = NetworkUtils.getResponseFromHttpUrl(params[2]);

            if (movieLengthResponse != null){
                JSONObject movieDatabaseResult = new JSONObject(movieLengthResponse);

                String runTime = movieDatabaseResult.getString("runtime");

                currentMovieObject.setMovieLength(runTime);

                }


            if (movieTrailerResponse != null){

                ArrayList<String> movieTrailers = new ArrayList<>();

                JSONObject movieTrailer = new JSONObject(movieTrailerResponse);
                JSONArray movieTrailerArray = movieTrailer.getJSONArray("results");

                for(int i = 0; i< movieTrailerArray.length(); i++){
                    JSONObject trailerObject = movieTrailerArray.getJSONObject(i);

                    String trailer = trailerObject.getString("key");
                    movieTrailers.add(trailer);
                }

                currentMovieObject.setTrailers(movieTrailers);

            }



            if (movieReviewsResponse != null){

                ArrayList<String> movieReviewsList = new ArrayList<>();

                JSONObject movieReviews = new JSONObject(movieReviewsResponse);

                JSONArray movieReviewsArray = movieReviews.getJSONArray("results");

                for (int i=0; i<movieReviewsArray.length(); i++){

                    JSONObject reviewObject = movieReviewsArray.getJSONObject(i);

                    String review = reviewObject.getString("content");

                    movieReviewsList.add(review);

                }

                currentMovieObject.setReviews(movieReviewsList);

            }

            } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return currentMovieObject;
        }


    }

    @Override
    protected void onPostExecute(MovieInfo movieInfos) {

        mbar.setVisibility(View.INVISIBLE);
        taskCompleted.onTaskCompletedDetailed(movieInfos);

    }

    public interface OnTaskCompletedDetail {
       void onTaskCompletedDetailed(MovieInfo response);
   }

}
