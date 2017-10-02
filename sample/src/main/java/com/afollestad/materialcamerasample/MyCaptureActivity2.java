package com.afollestad.materialcamerasample;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialcamera.CaptureActivity;
import com.afollestad.materialcamera.CaptureActivity2;
import com.afollestad.materialcamera.internal.Camera2Fragment;

/**
 * Created by mukund.jogi on 20/9/17.
 */

public class MyCaptureActivity2 extends CaptureActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRecording();
            }
        }, 10000);
    }

    //    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                stopRecording();
//            }
//        }, 10000);
//    }


    @Override
    protected void onNewIntent(Intent intent) {
        intent.getExtras();
        intent.getBooleanExtra("stop",false);
        super.onNewIntent(intent);
    }

    public void stopRecording() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof Camera2Fragment) {
            View container = fragment.getView();
            if (container == null) {
                return;
            }

            Button btnRecord = container.findViewById(R.id.video);

            btnRecord.performClick();
        }
    }
}
