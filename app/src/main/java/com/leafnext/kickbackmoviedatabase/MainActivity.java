package com.leafnext.kickbackmoviedatabase;

import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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


public class MainActivity extends AppCompatActivity implements GridViewAdapter.OnItemSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridViewFragment gridView = new GridViewFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.gridViewFragment, gridView);
        fragmentTransaction.commit();

        }


    @Override
    public void onMovieItemSelected(int position, MovieInfo info) {

        Movie_Detail_fragment movie_detail_fragment = new Movie_Detail_fragment();

        Bundle args = new Bundle();
        args.putString("position",info.getOriginalTitle());
        movie_detail_fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.gridViewFragment,movie_detail_fragment)
                .addToBackStack(null)
                .commit();



    }
}
