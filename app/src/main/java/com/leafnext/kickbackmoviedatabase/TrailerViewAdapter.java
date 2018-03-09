package com.leafnext.kickbackmoviedatabase;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by syedehteshamuddin on 2018-02-26.
 */

public class TrailerViewAdapter extends RecyclerView.Adapter<TrailerViewAdapter.MyViewHolder> {

    private ArrayList<String> mTrailerList;
    private Context mContext;

    TrailerViewAdapter(Context context){

        mContext = context;

    }

    public void setData(ArrayList<String> trailerList){
        mTrailerList = trailerList;
        notifyDataSetChanged();
    }

    @Override
    public TrailerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.trailer_item_view_horizontal,parent,false);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final TrailerViewAdapter.MyViewHolder holder, int position) {

         final String trailerid = mTrailerList.get(position);

         holder.trailerView.setText("Trailer "+ (position+1));
         holder.itemView.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
                 watchVideoYoutube(mContext,trailerid);
             }
         });

         if (position==0){
             holder.ellipsisView.setVisibility(View.VISIBLE);
             holder.ellipsisView.setOnClickListener(new OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Uri trailerUrl = Uri.parse("http://www.youtube.com/watch?v=" + trailerid);
                     Intent sendIntent = new Intent();
                     sendIntent.setAction(Intent.ACTION_SEND);
                     sendIntent.putExtra(Intent.EXTRA_TEXT,trailerUrl.toString());
                     sendIntent.setType("text/plain");
                     Intent.createChooser(sendIntent,"Share via");
                     if (sendIntent.resolveActivity(mContext.getPackageManager())!=null){
                         mContext.startActivity(sendIntent);
                     }

                     //    Toast.makeText(mContext,"share button is clicked",Toast.LENGTH_SHORT).show();
                 }
             });
         }



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
        ImageView ellipsisView;
        public MyViewHolder(View itemView) {
            super(itemView);

           trailerView = itemView.findViewById(R.id.trailerTitle);
           ellipsisView = itemView.findViewById(R.id.ellipsis);
        }
    }

    public void watchVideoYoutube(Context context, String id){

        Intent localAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(localAppIntent);
        }catch (ActivityNotFoundException ex){
            context.startActivity(webIntent);
        }
    }


}
