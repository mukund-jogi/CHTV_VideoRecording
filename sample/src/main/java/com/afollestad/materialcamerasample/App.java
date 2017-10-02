package com.afollestad.materialcamerasample;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by kevin.adesara on 02/10/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}

