package com.afollestad.materialcamerasample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import static android.R.attr.data;

public class Main3Activity extends AppCompatActivity {

    private int imageId;
    private String videoName;
    Main2Activity main2Activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Intent intent  = getIntent();
        intent.getExtras();

//        imageId  = new VideoListing(imageId.getImageResId());
//        videoName = new VideoListing(videoName.getVideoName());
//
//        Bitmap bmThumbnail;
//        bmThumbnail = ThumbnailUtils.createVideoThumbnail(intent.get, MediaStore.Video.Thumbnails.MICRO_KIND);
//        imageId.setImageBitmap(bmThumbnail);

        final ArrayList<VideoListing> videoList = new ArrayList<VideoListing>();
        while (!videoList.isEmpty()){
//            videoList.add(new VideoListing();
        }

    }
}
