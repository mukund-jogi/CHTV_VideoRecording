//package com.afollestad.materialcamerasample;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.media.ThumbnailUtils;
//import android.net.Uri;
//import android.provider.MediaStore;
//import android.support.annotation.IdRes;
//import android.support.annotation.LayoutRes;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.VideoView;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//import static android.R.attr.data;
//import static android.R.attr.resource;
//import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
//import static com.afollestad.materialcamerasample.R.id.imageView;
//import static com.afollestad.materialcamerasample.R.id.video;
//
///**
// * Created by mukund.jogi on 22/9/17.
// */
//
//public class MyAdapter extends ArrayAdapter<VideoListing> {
//
//    Bitmap bmThumbnail;
////    public MyAdapter(@NonNull Context context, ArrayList<VideoListing> videoList) {
////
////    }
//
//    public MyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<VideoListing> videoList) {
//        super(context, resource, videoList);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View listItems = convertView;
//        if(listItems == null)
//        {
//            listItems = LayoutInflater.from(getContext()).inflate(R.layout.listviewtems,parent,false);
//        }
//
//        VideoListing videoListing = getItem(position);
//
////        String path = String.valueOf(videoListing.getVideoName());
//        TextView videoNameUri = (TextView) listItems.findViewById(R.id.textView1);
////        videoNameUri.setText(path);
//        videoNameUri.setText(videoListing.getVideoName());
//
//
//        TextView videoStatus = (TextView) listItems.findViewById(R.id.textView2);
//        videoStatus.setText(videoListing.getVideoStatus());
//
////        ImageView imageView = (ImageView) listItems.findViewById(R.id.imageView);
////        imageView.setImageResource(videoListing.getImageResId());
//
//        VideoView videoView = (VideoView) listItems.findViewById(R.id.imageView) ;
//
////        videoView.setVideoURI(Uri.parse(String.valueOf(videoListing.getImageResId())));
//        videoView.setVideoURI(Uri.parse(videoListing.getVideoName()));
//
//        videoView.setVideoPath(videoListing.getVideoName());
//        videoView.setVisibility(View.VISIBLE);
//       // videoView.start();
//
////        videoView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////            }
////        });
//
////        if (videoListing.hasImage()) {
////            imageView.setImageResource(videoListing.getImageResId());
////            imageView.setVisibility(View.VISIBLE);
////        }
////        else
////            imageView.setVisibility(View.GONE);
//
//
//
////        return super.getView(position, convertView, parent);
//        return listItems;
//    }
//
//
//
//}
