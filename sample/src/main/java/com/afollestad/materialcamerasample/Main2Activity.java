package com.afollestad.materialcamerasample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialcamera.MaterialCamera;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.visibility_utils.calculator.DefaultSingleItemCalculatorCallback;
import com.volokh.danylo.visibility_utils.calculator.ListItemsVisibilityCalculator;
import com.volokh.danylo.visibility_utils.calculator.SingleListViewItemActiveCalculator;
import com.volokh.danylo.visibility_utils.scroll_utils.ItemsPositionGetter;
import com.volokh.danylo.visibility_utils.scroll_utils.RecyclerViewItemPositionGetter;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private final static int CAMERA_RQ = 6969;
    private final static String TEXT_RECORD = "Record";
    private final static String TEXT_PLAY = "Play";
    final ArrayList<BaseVideoItem> videoList1 = new ArrayList<>();
    private final ListItemsVisibilityCalculator mVideoVisibilityCalculator =
            new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), videoList1);
    public int imageId;
    RecyclerView recyclerView;
    TextView tvFileData;
    DBHelper myDBHelper;
    VideoPlayerManager<MetaData> videoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });

    private VideoView videoView;
    private Button btnPlay;
    private String server_Match_Id, server_Over;
    String sync_Status = "Local";
    /*BroadcastReceiver recordingStartReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("START_RECORDING")) {
                Toast.makeText(getApplicationContext(), "STARTRECORDING", Toast.LENGTH_LONG).show();
                server_Match_Id = intent.getStringExtra("match_id");
                server_Over = intent.getStringExtra("over");
                btnPlay.performClick();
            }
        }
    };*/
    private MaterialCamera materialCamera;
    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    private LinearLayoutManager mLayoutManager;
    private ItemsPositionGetter mItemsPositionGetter;

    // MQTT
    final String subscriptionTopic = "chtv";
    final String serverUri = "ws://cricheroes.in:1884";
//    final String serverUri = "ws://192.168.2.16:1884";
//    final String serverUri = "tcp://broker.hivemq.com:1883";
//    final String serverUri = "ws://broker.hivemq.com:8000";

    MqttAndroidClient mqttAndroidClient;
    String clientId = "chtv";

    //Spinner
    private AppCompatSpinner spVideo_Quality,spVideo_Bitrate;
    private List<VideoQuailtyList> selectVideoQuality;
    private List<VideoBitrateList> selectVideoBitrate;
    private int newVideoQuality = 0,newBitrate = 1000000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //To grant permissions
        isStoragePermissionGranted();



        setupControls();
        mqttImplementation();
    }

    private void setupControls() {




        //Toolbar view
        Toolbar toolbar = (Toolbar) findViewById(R.id.customToolbar);
        spVideo_Quality = (AppCompatSpinner) findViewById(R.id.spinner_ListQuality);
        spVideo_Bitrate = (AppCompatSpinner) findViewById(R.id.spinner_ListBitrate);
        setSupportActionBar(toolbar);

        setVideoQuality();
        setVideoBitrate();

        myDBHelper = new DBHelper(Main2Activity.this);

        //Video Player Setup
        videoView = (VideoView) findViewById(R.id.videoPlay);
        videoView.setMediaController(new MediaController(this));

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
        btnPlay.setVisibility(View.GONE);
        //btnPlay.setEnabled(false);

        /*LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(recordingStartReceiver, new IntentFilter("START_RECORDING"));*/


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If button has record then it will ecord or if it has play then it will play
                if (btnPlay.getText().equals(TEXT_RECORD)) {

                    materialCamera.start(CAMERA_RQ);


                } else if (btnPlay.getText().equals(TEXT_PLAY)) {
                    videoView.setVisibility(View.VISIBLE);
                    videoView.start();
                    btnPlay.setVisibility(View.GONE);

                }

            }
        });

        //Folder where video will be saved. In internal storage
        String path = Environment.getExternalStorageDirectory().toString() + "/CHTv";
        Log.d("Files", "Path: " + path);
        File saveFolder = new File(path);
        if (!saveFolder.mkdir()) {
            saveFolder.mkdir();
        }


        ArrayList<MatchInfo> matches = myDBHelper.getAllMatches();

        for (MatchInfo info : matches) {
            //Log.e("Matches",filesFromDir.getAbsolutePath()+ "____equals____" + info.getVideoUrl());
            videoList1.add(ItemFactory.createItemFromDir(info.getVideoUrl(), this, videoPlayerManager, R.mipmap.ic_launcher, info.toString()));
        }

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
                .autoRecordWithDelaySec(1);
//                .start(CAMERA_RQ);

//        autoStop();

        tvFileData = (TextView) findViewById(R.id.tvFileData);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        mLayoutManager = new LinearLayoutManager(Main2Activity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(videoPlayerManager, Main2Activity.this, videoList1);
        recyclerView.setAdapter(recyclerAdapter);
        mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager, recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                mScrollState = scrollState;
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE && !videoList1.isEmpty() && recyclerView.getVisibility() == View.VISIBLE) {

                    mVideoVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!videoList1.isEmpty() && recyclerView.getVisibility() == View.VISIBLE) {
                    mVideoVisibilityCalculator.onScroll(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition() + 1,
                            mScrollState);
                }
            }
        });
    }

    private void setVideoQuality() {
        selectVideoQuality = new ArrayList<>();
        selectVideoQuality.add(new VideoQuailtyList("Low_Quality",0));
        selectVideoQuality.add(new VideoQuailtyList("High_Quality",1));
        selectVideoQuality.add(new VideoQuailtyList("480p",4));
        selectVideoQuality.add(new VideoQuailtyList("720p",5));

        ArrayAdapter qualityAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, selectVideoQuality);
        qualityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVideo_Quality.setAdapter(qualityAdapter);
        spVideo_Quality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                newVideoQuality = selectVideoQuality.get(i).getVideoQuality();
                setNewVideoQuality(newVideoQuality);
                Log.e("Quality:", String.valueOf(newVideoQuality));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setVideoBitrate() {
        selectVideoBitrate = new ArrayList<>();
        selectVideoBitrate.add(new VideoBitrateList("1000000",1000000));
        selectVideoBitrate.add(new VideoBitrateList("2048000",2048000));
        selectVideoBitrate.add(new VideoBitrateList("2500000",2500000));
        selectVideoBitrate.add(new VideoBitrateList("5000000",5000000));

        ArrayAdapter bitrateAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, selectVideoBitrate);
        bitrateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVideo_Bitrate.setAdapter(bitrateAdapter);
        spVideo_Bitrate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                newBitrate = selectVideoBitrate.get(i).getVideoBitrate();
                setNewVideoBitrate(newBitrate);
                Log.e("Bitrate:", String.valueOf(newBitrate));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setNewVideoBitrate(int newBitrate) {
        Log.e("BitrateId:", String.valueOf(newBitrate));
        materialCamera.videoEncodingBitRate(newBitrate);
    }

    private void setNewVideoQuality(int videoQuality) {
        Log.e("QualityId:", String.valueOf(videoQuality));
        materialCamera.qualityProfile(videoQuality);
    }

    private void mqttImplementation() {
        clientId = clientId + System.currentTimeMillis();
        addToHistory("MQTT ClientId: " + clientId);
        mqttAndroidClient = new MqttAndroidClient(this, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverUri) {
                if(reconnect){
                    addToHistory("Reconnected to : " + serverUri);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                }else {
                    addToHistory("Connected to: " + serverUri);
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                addToHistory("Incoming message: " + new String(mqttMessage.getPayload()));

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });


        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);


        try {
            //addToHistory("Connecting to " + serverUri);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to connect to: " + serverUri);
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }

    }

    private void addToHistory(String mainText){
        System.out.println("LOG: " + mainText);
    }

    public void subscribeToTopic(){
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    addToHistory("Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to subscribe");
                }
            });

            // THIS DOES NOT WORK!
            mqttAndroidClient.subscribe(subscriptionTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // message Arrived!
                    addToHistory("Message: " + new String(message.getPayload()));
                    //System.out.println("Message: " + topic + " : " + new String(message.getPayload()));
                    handleMqttMessage(message);
                }
            });

        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    private MatchInfo mMatchInfo;
    private void handleMqttMessage(MqttMessage message) {
        try {
            JSONObject jsonMatchInfo = new JSONObject(new String(message.getPayload()));
            String command = jsonMatchInfo.optString("command", "");
            mMatchInfo = MatchInfo.fromJson(jsonMatchInfo);

            if (command.equalsIgnoreCase("START_OVER")) {
                addToHistory("Start over: " + jsonMatchInfo);
                materialCamera.start(CAMERA_RQ);
            } else {
                addToHistory("End over: " + jsonMatchInfo);
                Intent intent = new Intent("STOP_RECORDING");
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
                localBroadcastManager.sendBroadcast(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!videoList1.isEmpty() && recyclerView.getVisibility() == View.VISIBLE) {
            // need to call this method from list view handler in order to have filled list

            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mVideoVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("tag", "DATA:" + data);
        Log.i("tag", "resultCode:" + resultCode);

        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                imageId = 0;

                String newVideoPath = String.valueOf(data.getData());
                Log.i("tag", "DATA:" + newVideoPath);
                //https://medium.com/@v.danylo/implementing-video-playback-in-a-scrolled-list-listview-recyclerview-d04bc2148429
                //https://github.com/danylovolokh/VideoPlayerManager
                //https://github.com/eneim/toro
                btnPlay.setVisibility(View.GONE);

                // Message received > STOP_RECORDING Broadcase > onActivityResult of video recording
                if (mMatchInfo == null) {
                    mMatchInfo = new MatchInfo("0", "0", "", "");
                }
                mMatchInfo.setVideoUrl(newVideoPath);
                mMatchInfo.setSyncStatus(sync_Status);
                myDBHelper.insertMatchInfo(mMatchInfo);
                Log.e("Matches", String.valueOf(myDBHelper.getMatchesByStatus(sync_Status)));
                recyclerView.setVisibility(View.VISIBLE);

                videoList1.add(0, ItemFactory.createItemFromDir(newVideoPath, this, videoPlayerManager, R.mipmap.ic_launcher, mMatchInfo.toString()));
                recyclerView.getAdapter().notifyDataSetChanged();

            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error Getting Result", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        videoView.stopPlayback();
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) ) {
                Log.v("Permission","Permission is granted");
                return true;
            } else {

                Log.v("Permission","Permission is revoked");
                android.support.v13.app.ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission","Permission is granted");
            return true;
        }
    }


}

