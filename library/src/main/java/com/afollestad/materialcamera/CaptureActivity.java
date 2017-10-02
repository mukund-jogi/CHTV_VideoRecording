package com.afollestad.materialcamera;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialcamera.internal.BaseCaptureActivity;
import com.afollestad.materialcamera.internal.CameraFragment;

import static android.os.Build.VERSION_CODES.M;

public class CaptureActivity extends BaseCaptureActivity {

  @Override
  @NonNull
  public Fragment getFragment() {
    return CameraFragment.newInstance();
  }
}
