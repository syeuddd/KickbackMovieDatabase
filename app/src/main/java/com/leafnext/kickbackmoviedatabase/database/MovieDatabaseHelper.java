package com.leafnext.kickbackmoviedatabase.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leafnext.kickbackmoviedatabase.database.MovieDatabaseContract.MovieInfo;

/**
 * Created by syedehteshamuddin on 2018-02-22.
 */

    public class MovieDatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "movieinfo.db";

    public static final int DATABASE_VERSION = 1;


    public MovieDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIEINFO_TABLE = "CREATE TABLE " +
                MovieInfo.TABLE_NAME + "( " +
                MovieInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieInfo.COLUMN_MOVIE_TITLE + " TEXT, " +
                MovieInfo.COLUMN_MOVIE_THUMBNAIL + " TEXT, " +
                MovieInfo.COLUMN_MOVIE_POSTER + " TEXT, " +
                MovieInfo.COLUMN_MOVIE_RELEASE_DATE + " TEXT, " +
                MovieInfo.COLUMN_MOVIE_LENGTH + " TEXT, " +
                MovieInfo.COLUMN_MOVIE_RATING + " TEXT, " +
                MovieInfo.COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                MovieInfo.COLUMN_MOVIE_REVIEWS + " TEXT, " +
                MovieInfo.COLUMN_MOVIE_TRAILER_ONE + " TEXT, " +
                MovieInfo.COLUMN_MOVIE_TRAILER_TWO + " TEXT, " +
                MovieInfo.COLUMN_MOVIE_TRAILER_THREE + " TEXT " + ");";

        db.execSQL(SQL_CREATE_MOVIEINFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MovieInfo.TABLE_NAME);
        onCreate(db);

    }
}
