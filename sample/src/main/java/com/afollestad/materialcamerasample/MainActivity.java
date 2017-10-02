package com.afollestad.materialcamerasample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.afollestad.materialcamera.MaterialCamera;
import java.io.File;
import java.text.DecimalFormat;

/** @author Aidan Follestad (afollestad) */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private static final int CAMERA_RQ = 6969;
  private static final int PERMISSION_RQ = 84;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    findViewById(R.id.launchCamera).setOnClickListener(this);
    findViewById(R.id.launchCameraStillshot).setOnClickListener(this);
    findViewById(R.id.launchFromFragment).setOnClickListener(this);
    findViewById(R.id.launchFromFragmentSupport).setOnClickListener(this);

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      // Request permission to save videos in external storage
      ActivityCompat.requestPermissions(
          this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.launchFromFragment) {
      Intent intent = new Intent(this, FragmentActivity.class);
      startActivity(intent);
      return;
    }
    if (view.getId() == R.id.launchFromFragmentSupport) {
      Intent intent = new Intent(this, FragmentActivity.class);
      intent.putExtra("support", true);
      startActivity(intent);
      return;
    }

    File saveDir = null;

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED) {
      // Only use external storage directory if permission is granted, otherwise cache directory is used by default
      saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
      saveDir.mkdirs();
    }

    MaterialCamera materialCamera =
            new MaterialCamera(this) ;                              // Constructor takes an Activity
            materialCamera.allowRetry(true)                                  // Whether or not 'Retry' is visible during playback
              .autoSubmit(true)                                 // Whether or not user is allowed to playback videos after recording. This can affect other things, discussed in the next section.
              .saveDir(saveDir)                               // The folder recorded videos are saved to
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
              .autoRecordWithDelaySec(1)
              .audioDisabled(true)
            .start(CAMERA_RQ);                              // Set to true to record video without any audio.


    if (view.getId() == R.id.launchCameraStillshot)
      materialCamera
          .stillShot() // launches the Camera in stillshot mode
          .labelConfirm(R.string.mcam_use_stillshot);
    materialCamera.start(CAMERA_RQ);
  }

  private String readableFileSize(long size) {
    if (size <= 0) return size + " B";
    final String[] units = new String[] {"B", "KB", "MB", "GB", "TB"};
    int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
    return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups))
        + " "
        + units[digitGroups];
  }

  private String fileSize(File file) {
    return readableFileSize(file.length());
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Received recording or error from MaterialCamera
    if (requestCode == CAMERA_RQ) {
      if (resultCode == RESULT_OK) {
        final File file = new File(data.getData().getPath());
        Toast.makeText(
                this,
                String.format("Saved to: %s, size: %s", file.getAbsolutePath(), fileSize(file)),
                Toast.LENGTH_LONG)
            .show();
      } else if (data != null) {
        Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
        if (e != null) {
          e.printStackTrace();
          Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
      // Sample was denied WRITE_EXTERNAL_STORAGE permission
      Toast.makeText(
              this,
              "Videos will be saved in a cache directory instead of an external storage directory since permission was denied.",
              Toast.LENGTH_LONG)
          .show();
    }
  }
}
