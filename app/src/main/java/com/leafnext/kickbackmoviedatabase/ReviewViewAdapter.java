package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.leafnext.kickbackmoviedatabase.Utils.ResizeTextView;

import java.util.ArrayList;

/**
 * Created by syedehteshamuddin on 2018-02-26.
 */

public class ReviewViewAdapter extends RecyclerView.Adapter<ReviewViewAdapter.MyViewHolder> {

    private ArrayList<String> mTrailerList;
    private Context mContext;
    private TextView reviewTextView;
    boolean isTextViewClicked = false;

    ReviewViewAdapter(Context context){

        mContext = context;

    }

    public void setData(ArrayList<String> trailerList){
        mTrailerList = trailerList;
        notifyDataSetChanged();
    }

    @Override
    public ReviewViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.review_item_view,parent,false);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ReviewViewAdapter.MyViewHolder holder, int position) {
          final boolean isTextViewClicked = false;
         final String trailer = mTrailerList.get(position);

         reviewTextView = holder.trailerView;

         reviewTextView.setText(trailer);

         reviewTextView.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (ReviewViewAdapter.this.isTextViewClicked){
                    reviewTextView.setMaxLines(2);
                    ReviewViewAdapter.this.isTextViewClicked = false;
                 }else {
                     reviewTextView.setMaxLines(Integer.MAX_VALUE);
                     ReviewViewAdapter.this.isTextViewClicked = true;
                 }

             }
         });
    }

    @Override
    public int getItemCount() {
        if (mTrailerList==null){
            return 0;
        }

        return mTrailerList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView trailerView;
        public MyViewHolder(View itemView) {
            super(itemView);

           trailerView = itemView.findViewById(R.id.trailerTitle);
        }
    }
}
