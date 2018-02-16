package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leafnext.kickbackmoviedatabase.GridViewAdapter.OnItemSelectedListener;

/**
 * Created by syedehteshamuddin on 2018-02-14.
 */

public class Movie_Detail_fragment extends Fragment{

    private OnItemSelectedListener mListener;
    String movieTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        movieTitle = getArguments().getString("position");

        return inflater.inflate(R.layout.movie_detail_fragment_layout,container,false);



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        TextView originalMovieTitle = (TextView) view.findViewById(R.id.movieOriginalTitle);
        originalMovieTitle.setText(movieTitle);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener){
            mListener = (OnItemSelectedListener) context;
        }
    }

    public interface OnItemSelectedListener{
        void onMovieItemSelected(int position);
    }

}
