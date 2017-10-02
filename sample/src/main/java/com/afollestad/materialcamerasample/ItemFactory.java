package com.afollestad.materialcamerasample;

import android.app.Activity;

import com.squareup.picasso.Picasso;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

import java.io.IOException;

/**
 * Created by mukund.jogi on 29/9/17.
 */

class ItemFactory {

    public static BaseVideoItem createItemFromAsset(String assetName, int imageResource, Activity activity, VideoPlayerManager<MetaData> videoPlayerManager) throws IOException {
        return new AssetVideoItem(assetName, activity.getAssets().openFd(assetName), videoPlayerManager, Picasso.with(activity), imageResource);
    }

    public static BaseVideoItem createItemFromDir(String filePath, int position, Activity activity,VideoPlayerManager<MetaData> videoPlayerManager){
        return new AssetVideoItem(filePath,position,videoPlayerManager);
    }
}
