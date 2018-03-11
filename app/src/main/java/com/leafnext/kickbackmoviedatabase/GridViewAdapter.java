package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.leafnext.kickbackmoviedatabase.model.MovieInfo;
import com.squareup.picasso.Picasso;



import java.util.ArrayList;

 class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.MyViewHolder>{

    private ArrayList<MovieInfo> mMovieInfoArrayList;
    private Context mContext;
    private SQLiteDatabase favouriteDatabase;



     GridViewAdapter(Context context, SQLiteDatabase database){

        mContext = context;
        favouriteDatabase = database;

    }

    public void setData(ArrayList<MovieInfo> infos){
        mMovieInfoArrayList = infos;
        notifyDataSetChanged();

    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.grid_view_list_items,parent,false);

        return new MyViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final MovieInfo selectedMovie = mMovieInfoArrayList.get(position);



        String imageUrl = selectedMovie.getThumbnailImage();

        if (imageUrl.equals("null")){

            Picasso.with(mContext)
                    .load(R.drawable.image_not_available_128)
                    .into(holder.mImageView);
        }else {

            String baseUrl = "http://image.tmdb.org/t/p/w500";

            Uri baseUri = Uri.parse(baseUrl);

            Uri uri = Uri.withAppendedPath(baseUri, imageUrl);


            Picasso.with(mContext)
                    .load(uri)
                    .into(holder.mImageView);
            }

            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent movieDetailsIntent = new Intent(mContext,MovieDetailsActivity.class);

                    movieDetailsIntent.putExtra("movieDetails",selectedMovie);
                    if (selectedMovie.getMovieId()==null){
                        movieDetailsIntent.putExtra("dataFromDatabase",true);
                    }
                    mContext.startActivity(movieDetailsIntent);


                }
            });


    }


    @Override
    public int getItemCount() {

        if (mMovieInfoArrayList==null){
            return 0;
        }
        return mMovieInfoArrayList.size();
    }



     class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;

         MyViewHolder(View itemView){
            super(itemView);

            mImageView = itemView.findViewById(R.id.gridIconView);
        }

    }



}
