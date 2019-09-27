package com.expressivemoods;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;





public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mySongNames ;
    private ArrayList<String> mySongUrl ;
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> SongNames, ArrayList<String> SongUrl ) {
        mySongNames = SongNames;
        mySongUrl = SongUrl;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        holder.songName.setText(mySongNames.get(position));


        final MediaPlayer mediaPlayer = new MediaPlayer();




        holder.play.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {



                try {
                    mediaPlayer.setDataSource(mySongUrl.get(position));

                } catch (IOException e) {
                    e.printStackTrace();
                }



                if(!mediaPlayer.isPlaying()) {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    try {

                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    mediaPlayer.start();
                }

                Toast.makeText(mContext,"Play pressed "+mySongUrl.get(position),Toast.LENGTH_LONG).show();

            }
        });

        holder.pause.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                Toast.makeText(mContext,"Pause pressed",Toast.LENGTH_SHORT).show();

            }
        });

        holder.resume.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mediaPlayer.release();
                Toast.makeText(mContext,"Resume pressed",Toast.LENGTH_SHORT).show();

            }
        });






    }

    @Override
    public int getItemCount() {
        return mySongNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView songName;
        Button play,pause,resume;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            songName = itemView.findViewById(R.id.song_name);
            play = (Button)itemView.findViewById(R.id.btnPlay);
            pause = (Button)itemView.findViewById(R.id.btnPause);
            resume = (Button)itemView.findViewById(R.id.btnResume);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}















