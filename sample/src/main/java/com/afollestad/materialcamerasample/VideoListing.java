package com.afollestad.materialcamerasample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

/**
 * Created by mukund.jogi on 22/9/17.
 */

class VideoListing extends RecyclerView.ViewHolder {

    VideoPlayerView videoPlayerView;
    TextView fileName;
    TextView videoStatus;
    ImageView imageView;
    TextView visibility_percents;

    VideoListing(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        videoPlayerView =(VideoPlayerView) itemView.findViewById(R.id.videoViewPlayer);
        fileName = (TextView) itemView.findViewById(R.id.textView1);
        videoStatus =(TextView) itemView.findViewById(R.id.textView2);
        visibility_percents = (TextView) itemView.findViewById(R.id.visibility_percents);
    }
}
