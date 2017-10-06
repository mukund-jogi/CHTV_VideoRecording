package com.afollestad.materialcamerasample;

import android.util.Log;


/**
 * Created by kevin.adesara on 02/10/17.
 */

public class MyFirebaseInstanceIDService /*1extends FirebaseInstanceIdService*/ {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    /*@Override*/
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        /*String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);*/

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // TODO: Send push token to server
        // sendRegistrationToServer(refreshedToken);
    }
}
