package com.leafnext.kickbackmoviedatabase.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by syedehteshamuddin on 2018-02-22.
 */

public class MovieDatabaseContract{

    public static final String SCHEME = "content://";

    public static final String CONTENT_AUTHORITY = "com.leafnext.kickbackmoviedatabase";

    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME+CONTENT_AUTHORITY);

    public static final String PATH_TASKS = "movieDetails";


    public static final class MovieInfoContract implements BaseColumns{


        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_TASKS).build();


        public static final String TABLE_NAME = "movieDetails";

        public static final String COLUMN_MOVIE_TITLE="movieTitle";

        public static final String COLUMN_MOVIE_THUMBNAIL ="movieThumbnail";

        public static final String COLUMN_MOVIE_POSTER ="moviePoster";

        public static final String COLUMN_MOVIE_RELEASE_DATE="movieReleaseDate";

        public static final String COLUMN_MOVIE_RATING="movieRating";

        public static final String COLUMN_MOVIE_OVERVIEW="movieOverview";

        public static final String COLUMN_MOVIE_ID = "movieId";


    }
}
