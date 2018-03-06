package com.leafnext.kickbackmoviedatabase.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leafnext.kickbackmoviedatabase.database.MovieDatabaseContract.MovieInfoContract;

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
                MovieInfoContract.TABLE_NAME + "( " +
                MovieInfoContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieInfoContract.COLUMN_MOVIE_TITLE + " TEXT, " +
                MovieInfoContract.COLUMN_MOVIE_ID + " TEXT, " +
                MovieInfoContract.COLUMN_MOVIE_THUMBNAIL + " TEXT, " +
                MovieInfoContract.COLUMN_MOVIE_POSTER + " TEXT, " +
                MovieInfoContract.COLUMN_MOVIE_RELEASE_DATE + " TEXT, " +
                MovieInfoContract.COLUMN_MOVIE_RATING + " TEXT, " +
                MovieInfoContract.COLUMN_MOVIE_OVERVIEW + " TEXT " + ");";

        db.execSQL(SQL_CREATE_MOVIEINFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MovieInfoContract.TABLE_NAME);
        onCreate(db);

    }
}
