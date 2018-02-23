package com.leafnext.kickbackmoviedatabase.database;

import android.provider.BaseColumns;

/**
 * Created by syedehteshamuddin on 2018-02-22.
 */

public class MovieDatabaseContract{

    public static final class MovieInfo implements BaseColumns{

        public static final String TABLE_NAME = "movieDetails";

        public static final String COLUMN_MOVIE_TITLE="movieTitle";

        public static final String COLUMN_MOVIE_THUMBNAIL ="movieThumbnail";

        public static final String COLUMN_MOVIE_POSTER ="moviePoster";

        public static final String COLUMN_MOVIE_RELEASE_DATE="movieReleaseDate";

        public static final String COLUMN_MOVIE_LENGTH="movieLength";

        public static final String COLUMN_MOVIE_RATING="movieRating";

        public static final String COLUMN_MOVIE_OVERVIEW="movieOverview";

        public static final String COLUMN_MOVIE_TRAILER_ONE="movieTrailerOne";

        public static final String COLUMN_MOVIE_TRAILER_TWO = "movieTrailerTwo";

        public static final String COLUMN_MOVIE_TRAILER_THREE = "movieTrailerThree";

        public static final String COLUMN_MOVIE_REVIEWS = "movieReviews";

    }
}
