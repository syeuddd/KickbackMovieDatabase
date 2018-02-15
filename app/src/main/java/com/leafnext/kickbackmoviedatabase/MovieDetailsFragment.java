package com.leafnext.kickbackmoviedatabase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.leafnext.kickbackmoviedatabase.model.MovieInfo;

import java.util.ArrayList;

/**
 * Created by syedehteshamuddin on 2018-02-11.
 */

public class MovieDetailsFragment extends Fragment{

    private ArrayList<MovieInfo> mMovieInfoArrayList;
    String movieTitle;

    public MovieDetailsFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            movieTitle = getArguments().getString("position");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.movie_fragment_layout,container,false);

        return rootView;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView originalTitle = (TextView) view.findViewById(R.id.movieOriginalTitle);
        originalTitle.setText(movieTitle);
    }
}
