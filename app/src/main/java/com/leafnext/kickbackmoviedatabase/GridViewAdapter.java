package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.leafnext.kickbackmoviedatabase.model.MovieInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by syedehteshamuddin on 2018-02-13.
 */

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.MyViewHolder>{

    ArrayList<MovieInfo> mMovieInfoArrayList;
    Context mContext;
    private OnItemSelectedListener mListener;


    public GridViewAdapter(Context context, ArrayList<MovieInfo> movieInfo){

        mContext = context;
        mMovieInfoArrayList = movieInfo;

    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.grid_view_list_items,parent,false);

        MyViewHolder viewHolder = new MyViewHolder(rootView,mMovieInfoArrayList);

        mListener = (OnItemSelectedListener)mContext;

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        String imageUrl = mMovieInfoArrayList.get(position).getThumbnailImage();

        String baseUrl = "http://image.tmdb.org/t/p/w780";

        Uri baseUri = Uri.parse(baseUrl);

        Uri uri = Uri.withAppendedPath(baseUri,imageUrl);

        Picasso.with(mContext)
                .load(uri)
                .into(holder.mImageView);

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onMovieItemSelected(position, mMovieInfoArrayList.get(position));
            }
        });


    }


    @Override
    public int getItemCount() {
        return mMovieInfoArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;

        public MyViewHolder(View itemView, ArrayList<MovieInfo> movieInfo){
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.gridIconView);
        }

    }

    public interface OnItemSelectedListener{

        void onMovieItemSelected(int position, MovieInfo info);

    }



}
