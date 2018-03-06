package com.leafnext.kickbackmoviedatabase;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.review_item_view_linear_layout,parent,false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ReviewViewAdapter.MyViewHolder holder, int position) {
         final boolean isTextViewClicked = false;

         final String trailer = mTrailerList.get(position);

         //reviewTextView = holder.trailerView;

         holder.trailerView.setText(trailer);

        Log.i("ReviewViewAdapter",trailer.length()+"");

         holder.trailerView.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View view) {

                Log.i("DetailsActivity",trailer);
                 if (ReviewViewAdapter.this.isTextViewClicked){
                            holder.trailerView.setMaxLines(2);
                            ReviewViewAdapter.this.isTextViewClicked = false;
                 }else {

//                            int trailerLength = trailer.length();
//                            LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,trailerLength);
//
//                            ReviewViewAdapter.this.reviewTextView.setLayoutParams(params);
                            holder.trailerView.setMaxLines(Integer.MAX_VALUE);
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

    private boolean isTextEllipsied(){

        Layout layout = reviewTextView.getLayout();
        if (layout != null){
            int lines = layout.getLineCount();
            if (lines>0){
                if ((layout.getEllipsisCount(lines-1)>0)){
                    return true;
                }
            }
        }

        return false;
    }
}
