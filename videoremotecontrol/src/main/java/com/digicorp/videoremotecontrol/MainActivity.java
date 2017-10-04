package com.digicorp.videoremotecontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    private static final String API_BASE_URL = "http://192.168.0.30:3000";
    private static final String API_START_RECORDING = "/start-over";
    private static final String API_END_RECORDING = "/end-over";

    private Button btnStartRecording;
    private Button btnStopRecording;

    private JsonObject matchInfo = new JsonObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartRecording = (Button) findViewById(R.id.btnStartRecording);
        btnStopRecording = (Button) findViewById(R.id.btnStopRecording);
        btnStopRecording.setEnabled(false);
        prepareMatchData();
    }

    private void prepareMatchData() {
        matchInfo.addProperty("match_id", "10");
        matchInfo.addProperty("team_a", "DG-Lion");
        matchInfo.addProperty("team_b", "DG-Tiger");
        matchInfo.addProperty("score", "100 / 3");
        matchInfo.addProperty("over", "10");
        matchInfo.addProperty("batting", "DG-Lion");
    }

    public void startRecording(View view) {
        btnStartRecording.setEnabled(false);
        btnStopRecording.setEnabled(true);
        Ion.with(this)
                .load(API_BASE_URL + API_START_RECORDING)
                .setJsonObjectBody(matchInfo)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject jsonObject) {
                        if (e != null) {
                            Log.d("START_RECORDING", "Error: " + e);
                            return;
                        }

                        Log.d("START_RECORDING", "Response: " + jsonObject);

                    }
                });
    }

    public void stopRecording(View view) {
        btnStartRecording.setEnabled(true);
        btnStopRecording.setEnabled(false);
        Ion.with(this)
                .load(API_BASE_URL + API_END_RECORDING)
                .setJsonObjectBody(matchInfo)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject jsonObject) {
                        if (e != null) {
                            Log.d("END_RECORDING", "Error: " + e);
                            return;
                        }

                        Log.d("END_RECORDING", "Response: " + jsonObject);
                    }
                });
    }
}
