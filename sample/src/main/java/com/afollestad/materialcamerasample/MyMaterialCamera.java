package com.afollestad.materialcamerasample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.afollestad.materialcamera.CaptureActivity;
import com.afollestad.materialcamera.CaptureActivity2;
import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialcamera.internal.CameraIntentKey;
import com.afollestad.materialcamera.util.CameraUtil;

/**
 * Created by mukund.jogi on 20/9/17.
 */

public class MyMaterialCamera extends MaterialCamera {
    private Activity context;
    public MyMaterialCamera(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    public Intent getIntent() {

        Intent intent = super.getIntent();
        Intent newIntent = new Intent(context, MyCaptureActivity2.class);
//        newIntent.getBooleanExtra(intent.getStringExtra("stopRecording"),true);/
        newIntent.putExtras(intent.getExtras());
        return newIntent;
    }


    @Override
    public MaterialCamera start(int requestCode) {
        context.startActivityForResult(getIntent(), requestCode);
        return this;
    }
}
