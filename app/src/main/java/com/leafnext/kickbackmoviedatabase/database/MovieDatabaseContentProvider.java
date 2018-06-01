package com.leafnext.kickbackmoviedatabase.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.leafnext.kickbackmoviedatabase.R;
import com.leafnext.kickbackmoviedatabase.database.MovieDatabaseContract.MovieInfoContract;

/**
 * Created by ehteshs1 on 2018/03/02.
 */

public class MovieDatabaseContentProvider extends ContentProvider{

    private MovieDatabaseHelper mMovieDatabaseHelper;

    private static final int MOVIEDETAILS = 100;

    public static final int MOVIEDETAILSWITHID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieDatabaseContract.CONTENT_AUTHORITY,MovieDatabaseContract.PATH_TASKS,MOVIEDETAILS);

        uriMatcher.addURI(MovieDatabaseContract.CONTENT_AUTHORITY,MovieDatabaseContract.PATH_TASKS + "/#",MOVIEDETAILSWITHID);

        return uriMatcher;

    }


    @Override
    public boolean onCreate() {

        Context context = getContext();
        mMovieDatabaseHelper = new MovieDatabaseHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        final SQLiteDatabase db = mMovieDatabaseHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch (match){
            case MOVIEDETAILS:

            returnCursor = db.query(MovieInfoContract.TABLE_NAME,strings,s,strings1,s1,null,null);

            if (returnCursor==null){
                throw new SQLException("Failed to fetch rows" + uri);
            }
                break;
            default:throw new UnsupportedOperationException("Unkown uri "+ uri);
        }


        return returnCursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase db = mMovieDatabaseHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case MOVIEDETAILS:

            long id = db.insert(MovieInfoContract.TABLE_NAME,null,contentValues);

             if (id>0){

                 returnUri = ContentUris.withAppendedId(MovieInfoContract.CONTENT_URI,id);

             }else {
                 throw new SQLException("Failed to insert row into " + uri);
             }

                break;

            default:throw new UnsupportedOperationException("Unkown uri :" + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        final SQLiteDatabase db = mMovieDatabaseHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int numberOfRowsDelted = 0;

        switch (match){
            case MOVIEDETAILS:

                break;
            case MOVIEDETAILSWITHID:

              numberOfRowsDelted = db.delete(MovieInfoContract.TABLE_NAME,s,strings);

              if (numberOfRowsDelted>0){
                  Toast.makeText(getContext(), R.string.favorites_removed_msg,Toast.LENGTH_SHORT).show();
              }


                break;
            default:throw new UnsupportedOperationException("Unknown uri"+ uri);
        }




        return numberOfRowsDelted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        // no user generated content to update database

        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
