package com.afollestad.materialcamerasample;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Map;

/**
 * Created by kevin.adesara on 02/10/17.
 */

public class MyFirebaseMessagingService /*extends FirebaseMessagingService*/ {

    /*@Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("FCM", "Data: " + remoteMessage.getData().toString());
        Log.d("FCM", "Body: " + remoteMessage.getNotification().getBody());

        Map<String, String> data = remoteMessage.getData();
        String dattMess = String.valueOf(remoteMessage.getData());
        String command = data.get("command");


        if (command.equalsIgnoreCase("START_RECORDING")) {
            Intent intent = new Intent("START_RECORDING");
            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this);
            localBroadcastManager.sendBroadcast(intent);

        } else if (command.equalsIgnoreCase("END_RECORDING")) {
            // This broadcast is handled in library module > BaseCameraFragment
            Intent intent = new Intent("STOP_RECORDING");
//            intent.putExtra
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this);
            localBroadcastManager.sendBroadcast(intent);
        }
    }*/
}
