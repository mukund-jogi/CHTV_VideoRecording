package com.afollestad.materialcamerasample;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URISyntaxException;

/**
 * Created by kevin.adesara on 02/10/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static String fcmData;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("FCM", "Data: " + remoteMessage.getData().toString());
        Log.d("FCM", "Body: " + remoteMessage.getNotification().getBody());


        // TODO: Send local broadcast to start/stop video recording
        String fcmMessage = remoteMessage.getNotification().getBody().toString();
        fcmData = remoteMessage.getData().toString();

        if(fcmMessage.equalsIgnoreCase("Start"))
        {
            LocalBroadcastManager localBroadCcastManager= LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this);
            localBroadCcastManager.sendBroadcast(new Intent("START_RECORDING"));
        }

        else if(fcmMessage.equalsIgnoreCase("Stop"))
        {
            Intent intent = new Intent("STOP_RECORDING");
            intent.putExtra(fcmData,true);
            LocalBroadcastManager localBroadcastManager= LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this);
            localBroadcastManager.sendBroadcast(intent);
        }

    }
}
