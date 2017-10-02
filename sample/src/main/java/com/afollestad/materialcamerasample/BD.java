package com.afollestad.materialcamerasample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;

/**
 * Created by mukund.jogi on 18/9/17.
 */

public class BD extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context ,"BroadcastRecieved",Toast.LENGTH_LONG).show();
        Log.d("tag","thifsdofli");
        context.startActivity(intent);
//        startActivity(intent);


    }
}
