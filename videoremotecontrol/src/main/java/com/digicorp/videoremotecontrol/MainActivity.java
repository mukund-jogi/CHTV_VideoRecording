package com.digicorp.videoremotecontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    private static final String API_BASE_URL = "http://192.168.0.30:3000";
    private static final String API_START_OVER = "/start-over";
    private static final String API_END_OVER = "/end-over";

    private EditText txtTeamA;
    private EditText txtTeamB;
    private EditText txtOver;
    private EditText txtScore;
    private Button btnStartRecording;
    private Button btnStopRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartRecording = (Button) findViewById(R.id.btnStartRecording);
        btnStopRecording = (Button) findViewById(R.id.btnStopRecording);
        txtTeamA = (EditText) findViewById(R.id.txtTeamA);
        txtTeamB = (EditText) findViewById(R.id.txtTeamB);
        txtOver = (EditText) findViewById(R.id.txtOver);
        txtScore = (EditText) findViewById(R.id.txtScore);
        btnStopRecording.setEnabled(false);
    }

    private JsonObject prepareMatchData() {
        JsonObject matchInfo = new JsonObject();
        matchInfo.addProperty("match_id", "10");
        matchInfo.addProperty("team_a", txtTeamA.getText().toString().trim().length() == 0 ? "DG-Lion" : txtTeamA.getText().toString().trim());
        matchInfo.addProperty("team_b", txtTeamB.getText().toString().trim().length() == 0 ? "DG-Tiger" : txtTeamB.getText().toString().trim());
        matchInfo.addProperty("score", txtScore.getText().toString().trim());
        matchInfo.addProperty("over", txtOver.getText().toString().trim());
        matchInfo.addProperty("batting", txtTeamA.getText().toString().trim());
        return matchInfo;
    }

    public void startRecording(View view) {
        JsonObject matchInfo = prepareMatchData();
        btnStartRecording.setEnabled(false);
        btnStopRecording.setEnabled(true);
        Ion.with(this)
                .load(API_BASE_URL + API_START_OVER)
                .setJsonObjectBody(matchInfo)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject jsonObject) {
                        if (e != null) {
                            Log.d("START_RECORDING", "Error: " + e);
                            Toast.makeText(MainActivity.this, "Error in start recording", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.d("START_RECORDING", "Response: " + jsonObject);
                        Toast.makeText(MainActivity.this, "Recording started", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void stopRecording(View view) {
        JsonObject matchInfo = prepareMatchData();
        btnStartRecording.setEnabled(true);
        btnStopRecording.setEnabled(false);
        Ion.with(this)
                .load(API_BASE_URL + API_END_OVER)
                .setJsonObjectBody(matchInfo)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject jsonObject) {
                        if (e != null) {
                            Log.d("END_RECORDING", "Error: " + e);
                            Toast.makeText(MainActivity.this, "Error in stop recording", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.d("END_RECORDING", "Response: " + jsonObject);
                        Toast.makeText(MainActivity.this, "Recording stopped", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
