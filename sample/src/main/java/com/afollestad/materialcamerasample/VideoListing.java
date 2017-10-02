package com.afollestad.materialcamerasample;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

/**
 * Created by mukund.jogi on 22/9/17.
 */

public class VideoListing extends RecyclerView.ViewHolder {

//    private String fileName;
//    private String fileStatus;
//    String fileName;
    public VideoPlayerView videoPlayerView;
    public TextView fileName;
    public TextView videoStatus;
    public ImageView imageView;
    public TextView visibility_percents;

    public VideoListing(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        videoPlayerView =(VideoPlayerView) itemView.findViewById(R.id.videoViewPlayer);
        fileName = (TextView) itemView.findViewById(R.id.textView1);
        videoStatus =(TextView) itemView.findViewById(R.id.textView2);
        visibility_percents = (TextView) itemView.findViewById(R.id.visibility_percents);
    }



//    private static final int NOIMAGEID = -1;
//    private int imageResId = NOIMAGEID;
//
//    public VideoListing(int imageId, String videoName, String videoStatus){
//        fileName = videoName;
//        fileStatus = videoStatus;
//        imageResId = imageId;
//    }
//
//    public String getVideoName(){return fileName;}
//
//    public String getVideoStatus(){return fileStatus;}
//
//    public int getImageResId() {return imageResId;}
//
//    public boolean hasImage()
//    {
//        return imageResId!=NOIMAGEID;
//    }



}
