package com.afollestad.materialcamerasample;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by kevin.adesara on 02/10/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("FCM", "Data: " + remoteMessage.getData().toString());
        Log.d("FCM", "Body: " + remoteMessage.getNotification().getBody());

        // TODO: Send local broadcast to start/stop video recording
    }
}
