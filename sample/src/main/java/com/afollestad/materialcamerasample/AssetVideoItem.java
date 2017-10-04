package com.afollestad.materialcamerasample;

import android.content.res.AssetFileDescriptor;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.volokh.danylo.video_player_manager.Config;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;
import com.volokh.danylo.video_player_manager.utils.Logger;

import java.io.File;

/**
 * Created by mukund.jogi on 29/9/17.
 */

public class AssetVideoItem extends BaseVideoItem {
    private static final String TAG = AssetVideoItem.class.getSimpleName();
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;

    private AssetFileDescriptor mAssetFileDescriptor;
    private String mTitle;
    private String mVideoFilePath;
    private String myUpdatedValue;
    private Picasso mImageLoader;
    private int mImageResource;
    MyFirebaseMessagingService myFirebaseMessagingService =new MyFirebaseMessagingService();

    public AssetVideoItem(String title, AssetFileDescriptor assetFileDescriptor, VideoPlayerManager<MetaData> videoPlayerManager, Picasso imageLoader, int imageResource) {
        super(videoPlayerManager);
        mTitle = title;
        mAssetFileDescriptor = assetFileDescriptor;
        mImageLoader = imageLoader;
        mImageResource = imageResource;
    }

    public AssetVideoItem(String filePath, VideoPlayerManager<MetaData> videoPlayerManager, Picasso imageLoader, int imageResource, String data) {
        super(videoPlayerManager);
        mVideoFilePath = filePath;
        mTitle = new File(filePath).getName();
        myUpdatedValue = data;
        mImageLoader = imageLoader;
        mImageResource = imageResource;
    }

    @Override
    public void update(int position, final VideoListing viewHolder, VideoPlayerManager videoPlayerManager) {
        if (SHOW_LOGS) Logger.v(TAG, "update, position " + position);

        viewHolder.fileName.setText(mTitle);
        viewHolder.fileName.setVisibility(View.VISIBLE);
        viewHolder.imageView.setVisibility(View.VISIBLE);
        mImageLoader.load(mImageResource).into(viewHolder.imageView);

//        viewHolder.videoStatus.setText(new MyFirebaseMessagingService().fcmData);
//        viewHolder.videoStatus.setVisibility(View.VISIBLE);

        viewHolder.fileName.setText(mTitle);
        viewHolder.fileName.setVisibility(View.VISIBLE);
        viewHolder.imageView.setVisibility(View.VISIBLE);
        mImageLoader.load(mImageResource).into(viewHolder.imageView);
        viewHolder.videoStatus.setText(myUpdatedValue);
        viewHolder.videoStatus.setVisibility(View.VISIBLE);
    }


    @Override
    public void playNewVideo(MetaData currentItemMetaData, VideoPlayerView player, VideoPlayerManager<MetaData> videoPlayerManager) {
        videoPlayerManager.playNewVideo(currentItemMetaData, player, mVideoFilePath);
    }

    @Override
    public void stopPlayback(VideoPlayerManager videoPlayerManager) {
        videoPlayerManager.stopAnyPlayback();
    }

    @Override
    public String toString() {
        return getClass() + ", mTitle[" + myUpdatedValue + "]";
    }
}
