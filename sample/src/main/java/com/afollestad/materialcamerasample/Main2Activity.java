package com.afollestad.materialcamerasample;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialcamera.internal.BaseCaptureActivity;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;
import com.volokh.danylo.visibility_utils.calculator.DefaultSingleItemCalculatorCallback;
import com.volokh.danylo.visibility_utils.calculator.ListItemsVisibilityCalculator;
import com.volokh.danylo.visibility_utils.calculator.SingleListViewItemActiveCalculator;
import com.volokh.danylo.visibility_utils.scroll_utils.ItemsPositionGetter;
import com.volokh.danylo.visibility_utils.scroll_utils.RecyclerViewItemPositionGetter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;


public class Main2Activity extends AppCompatActivity {

    private final static int CAMERA_RQ = 6969;
    public final static int PICK_VIDEO_PATH = 999;
    private final static String TEXT_RECORD = "Record";
    private final static String TEXT_PLAY = "Play";
    private VideoView videoView;
    private Button btnPlay;
    private MaterialCamera materialCamera;
    private Timer timer;
    private  TimerTask timerTask;
    public int imageId;
    private BroadcastReceiver broadcastReceiver;
    public boolean isDelayTime=false;
    public ArrayList<Uri> videoPath;
    public String filePath;
    ListView listView;
    RecyclerView recyclerView;

    Uri savedFiles;
    ImageView imageList ;
    Bitmap bmThumbnail;
    File saveFolder = null;
    File[] files =null;
    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    final ArrayList<VideoListing> videoList = new ArrayList<VideoListing>();
    final ArrayList<BaseVideoItem> videoList1 = new ArrayList<BaseVideoItem>();
    private final ListItemsVisibilityCalculator mVideoVisibilityCalculator =
            new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), videoList1);
    private LinearLayoutManager mLayoutManager;
    private ItemsPositionGetter mItemsPositionGetter;

    VideoPlayerManager<MetaData> videoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


//        Uri pathOfPreviousVideos =
        //Video Player Setup
        videoView = (VideoView) findViewById(R.id.videoPlay);
        videoView.setMediaController(new MediaController(this));
//        videoList.add(new VideoListing(imageId,data.getData(),"Uploaded"));

        //On Video completion close video layer and show button again
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnPlay.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
            }
        });

        btnPlay = (Button) findViewById(R.id.btnPlay);

        //First Button should record.
        btnPlay.setText(TEXT_RECORD);


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If button has record then it will ecord or if it has play then it will play
                if(btnPlay.getText().equals(TEXT_RECORD)){

                    materialCamera.start(CAMERA_RQ);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(Main2Activity.this);
                            broadcastManager.sendBroadcast(new Intent("STOP_RECORDING"));

                        }
                    }, 10000);
//


                }else if(btnPlay.getText().equals(TEXT_PLAY)){
                    videoView.setVisibility(View.VISIBLE);
                    videoView.start();
                    btnPlay.setVisibility(View.GONE);

                }

            }
        });


        //Folder where video will be saved. In internal storage
        String path = Environment.getExternalStorageDirectory().toString()+"/MaterialCameraVids";
        Log.d("Files", "Path: " + path);
        File saveFolder = new File(path);
//        saveFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/MaterialCameraVids");
        if(!saveFolder.mkdir()){
//            saveFolder = Environment.getExternalStorageDirectory();
            saveFolder.mkdir();
        }

//        File myfile = new File(saveFolder.getAbsolutePath());
//        if(myfile.isDirectory()) {
//            files = saveFolder.listFiles();
            for (File f : saveFolder.listFiles()) {
//                if (f.isFile())

                    filePath = f.getPath();
                videoList1.add(ItemFactory.createItemFromDir(filePath,0,Main2Activity.this,videoPlayerManager));
                //                videoList1.add(new VideoListing(imageId, filePath, "Uploaded"));
                Log.d("File","FileName:"+f.getName());;
//            for (int i = 0; i < files.length; i++) {
//                filePath = files[i].getName();
//                savedFiles = Uri.parse(filePath);
            }



     /*   saveFolder.deleteOnExit();
        if (!saveFolder.mkdirs())
            throw new RuntimeException("Unable to create save directory, make sure WRITE_EXTERNAL_STORAGE permission is granted.");*/
        materialCamera = new MaterialCamera(this);                               // Constructor takes an Activity
        materialCamera.allowRetry(true)                                  // Whether or not 'Retry' is visible during playback
                .autoSubmit(true)                                 // Whether or not user is allowed to playback videos after recording. This can affect other things, discussed in the next section.
                .saveDir(saveFolder)                               // The folder recorded videos are saved to
                .primaryColorAttr(R.attr.colorPrimary)             // The theme color used for the camera, defaults to colorPrimary of Activity in the constructor
                .showPortraitWarning(false)                         // Whether or not a warning is displayed if the user presses record in portrait orientation
                .defaultToFrontFacing(false)                       // Whether or not the camera will initially show the front facing camera
                .retryExits(false)                                 // If true, the 'Retry' button in the playback screen will exit the camera instead of going back to the recorder
                .restartTimerOnRetry(false)                        // If true, the countdown timer is reset to 0 when the user taps 'Retry' in playback
                .continueTimerInPlayback(false)                    // If true, the countdown timer will continue to go down during playback, rather than pausing.
                .iconRecord(R.drawable.mcam_action_capture)        // Sets a custom icon for the button used to start recording
                .iconStop(R.drawable.mcam_action_stop)             // Sets a custom icon for the button used to stop recording
                .iconFrontCamera(R.drawable.mcam_camera_front)     // Sets a custom icon for the button used to switch to the front camera
                .iconRearCamera(R.drawable.mcam_camera_rear)       // Sets a custom icon for the button used to switch to the rear camera
                .iconPlay(R.drawable.evp_action_play)              // Sets a custom icon used to start playback
                .iconPause(R.drawable.evp_action_pause)            // Sets a custom icon used to pause playback
                .iconRestart(R.drawable.evp_action_restart)        // Sets a custom icon used to restart playback
                .labelRetry(R.string.mcam_retry)                   // Sets a custom button label for the button used to retry recording, when available
                .labelConfirm(R.string.mcam_use_video)             // Sets a custom button label for the button used to confirm/submit a recording
                .audioDisabled(true)                               // Set to true to record video without any audio.
                .autoRecordWithDelaySec(1)
                .videoEncodingBitRate(1000000);
//                .start(CAMERA_RQ);

//        autoStop();


    }



    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("tag","DATA:"+data);
        Log.i("tag","resultCode:"+resultCode);

//        Intent getStopIntent = new Intent();
//        if(getStopIntent.getFlags()==1)



        if(requestCode == CAMERA_RQ){
            if(resultCode == RESULT_OK){
//                Intent passMain3Activity =   new Intent(Intent.ACTION_GET_CONTENT);
//                passMain3Activity.setType("Video/*");
//                startActivityForResult(passMain3Activity,PICK_VIDEO_PATH);

//                Intent passStartActivity = new Intent(this,Main3Activity.class);
//                startActivity(passStartActivity);

//                filePath = String.valueOf(data.getData());
//                Intent sendFilePath = new Intent(this,MyAdapter.class);
//                sendFilePath.putExtra(filePath,true);
//                startActivity(sendFilePath);

//                bmThumbnail = ThumbnailUtils.createVideoThumbnail(String.valueOf(data.getData()),MediaStore.Video.Thumbnails.MICRO_KIND);

//                imageList = (ImageView) findViewById(R.id.imageView);
//                imageList.setImageBitmap(bmThumbnail);
//                imageId = imageList.getId();
                imageId = 0;
                String newVideo = String.valueOf(data.getData());

//                videoList.add(new VideoListing(imageId,newVideo,"Uploaded"));


                //https://medium.com/@v.danylo/implementing-video-playback-in-a-scrolled-list-listview-recyclerview-d04bc2148429
                //https://github.com/danylovolokh/VideoPlayerManager
                //https://github.com/eneim/toro




//                MyAdapter myAdapter = new MyAdapter(Main2Activity.this,0,videoList);
//
//                listView = (ListView) findViewById(R.id.listView1);
//                listView.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.GONE);
//                listView.setAdapter(myAdapter);

                mLayoutManager = new LinearLayoutManager(Main2Activity.this);
                recyclerView.setLayoutManager(mLayoutManager);

                RecyclerAdapter recyclerAdapter =new RecyclerAdapter(videoPlayerManager,Main2Activity.this,videoList1);
                recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(recyclerAdapter);


                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){@Override
                public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                    mScrollState = scrollState;
                    if(scrollState == RecyclerView.SCROLL_STATE_IDLE && !videoList1.isEmpty()){

                        mVideoVisibilityCalculator.onScrollStateIdle(
                                mItemsPositionGetter,
                                mLayoutManager.findFirstVisibleItemPosition(),
                                mLayoutManager.findLastVisibleItemPosition());
                    }
                }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if(!videoList1.isEmpty()){
                            mVideoVisibilityCalculator.onScroll(
                                    mItemsPositionGetter,
                                    mLayoutManager.findFirstVisibleItemPosition(),
                                    mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition() + 1,
                                    mScrollState);
                        }
                    }
                });

                mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager, recyclerView);


                /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        VideoListing videoListing = videoList.get(position);


                        listView.setVisibility(View.GONE);
                        Uri videoUri = Uri.parse(videoListing.getVideoName());
                        videoView.setVideoURI(videoUri);
                        videoView.setVisibility(View.VISIBLE);

                        videoView.start();

                    }
                } );*/

//                Intent passStartActivity = new Intent(this,Main3Activity.class);
//                passStartActivity.putExtra(String.valueOf(videoPath),true);
//                startActivity(passStartActivity);

//                videoView.setVideoURI(data.getData());
//                btnPlay.setText(TEXT_PLAY);
            }
            else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            else {
//                videoView.setVideoURI(data.getData());
//                btnPlay.setText(TEXT_PLAY);
                Toast.makeText(getApplicationContext(),"Error Getting Result", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void autoStop()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final BaseCaptureActivity baseCaptureActivity = new BaseCaptureActivity() {
                    @NonNull
                    @Override
                    public Fragment getFragment() {

                        return null;
                    }
                };

//                materialCamera.countdownSeconds(1);
            }
        }, 10 * 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoView.stopPlayback();
    }
}

