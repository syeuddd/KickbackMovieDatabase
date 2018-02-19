package com.leafnext.kickbackmoviedatabase.Utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {



    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";

    public static final String TOP_RATED_MOVIES = "top_rated";

    public static final String POPULAR_MOVIES = "popular";

    private static final String API_KEY_PARAMETER="api_key";

    private static final String API_KEY = "d1cd092a990783ac43ec54e4c4009145";



public static URL buildUrl(String query){

    Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
            .appendPath(query)
            .appendQueryParameter(API_KEY_PARAMETER,API_KEY)
            .build();

    URL url = null;

    try{
        url = new URL(builtUri.toString());
    } catch (MalformedURLException e) {
        e.printStackTrace();
    }

    return url;

}


public static String getResponseFromHttpUrl(URL url) throws IOException{

    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

    try {
        InputStream in = urlConnection.getInputStream();

        Scanner scanner = new Scanner(in);

        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        if (hasInput){
            return scanner.next();
        }else {
            return null;
        }
    }finally {
        urlConnection.disconnect();
    }

}




}
