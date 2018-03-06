package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.leafnext.kickbackmoviedatabase.Utils.NetworkUtils;
import com.leafnext.kickbackmoviedatabase.database.MovieDatabaseContract.MovieInfoContract;
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

public class FetchMoviesFromDatabase extends AsyncTaskLoader<ArrayList<MovieInfo>> {

    Context mContext;
    ProgressBar mProgressBar;


    public FetchMoviesFromDatabase(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<MovieInfo> loadInBackground() {
        Cursor cursor = mContext.getContentResolver().query(MovieInfoContract.CONTENT_URI,null,null,null,null,null);
        ArrayList<MovieInfo> favouriteMovieList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {

                int titleIndex = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_TITLE);
                int movieIdIndex = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_ID);
                int thumbnailIndex = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_THUMBNAIL);
                int posterIndex = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_POSTER);
                int overviewIndex = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_OVERVIEW);
                int userRatingIndex = cursor.getColumnIndexOrThrow(MovieInfoContract.COLUMN_MOVIE_RATING);
                int releaseDateIndex = cursor.getColumnIndex(MovieInfoContract.COLUMN_MOVIE_RELEASE_DATE);

                String title = cursor.getString(titleIndex);
                String movieId = cursor.getString(movieIdIndex);
                String thumbnail = cursor.getString(thumbnailIndex);
                String poster = cursor.getString(posterIndex);
                String overview = cursor.getString(overviewIndex);
                String userRating = cursor.getString(userRatingIndex);
                String releaseDate = cursor.getString(releaseDateIndex);

                MovieInfo movieInfo = new MovieInfo();
                movieInfo.setOriginalTitle(title);
                movieInfo.setMovieId(movieId);
                movieInfo.setThumbnailImage(thumbnail);
                movieInfo.setPosterImage(poster);
                movieInfo.setOverview(overview);
                movieInfo.setVoteAverage(userRating);
                movieInfo.setReleaseDate(releaseDate);


                favouriteMovieList.add(i,movieInfo);
                if (!cursor.moveToNext()){
                    cursor.close();
                    return favouriteMovieList;
                }


            }

        }
        cursor.close();
        return favouriteMovieList;
    }


    @Override
    public void deliverResult(ArrayList<MovieInfo> data) {
        super.deliverResult(data);
    }


}
