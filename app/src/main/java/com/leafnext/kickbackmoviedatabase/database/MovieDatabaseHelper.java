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
                MovieInfo.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieInfo.COLUMN_MOVIE_THUMBNAIL + " TEXT NOT NULL, " +
                MovieInfo.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MovieInfo.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieInfo.COLUMN_MOVIE_LENGTH + " TEXT NOT NULL, " +
                MovieInfo.COLUMN_MOVIE_RATING + " TEXT NOT NULL, " +
                MovieInfo.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieInfo.COLUMN_MOVIE_REVIEWS + " TEXT NOT NULL, " +
                MovieInfo.COLUMN_MOVIE_TRAILER_ONE + " TEXT NOT NULL, " +
                MovieInfo.COLUMN_MOVIE_TRAILER_TWO + " TEXT NOT NULL, " +
                MovieInfo.COLUMN_MOVIE_TRAILER_THREE + " TEXT NOT NULL" + ");";

        db.execSQL(SQL_CREATE_MOVIEINFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MovieInfo.TABLE_NAME);
        onCreate(db);

    }
}
