package com.afollestad.materialcamerasample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by mukund.jogi on 3/10/17.
 */

class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "chtv.sqlite.db";
    public static final String FILEPATH = "video_url";
    public static final String VIDEODATA = "updatedData";
    public static final String MATCH_ID = "match_id";
    public static final String OVER = "over";
    public static final String SYNC_STATUS = "sync_status";

    // TODO: 0. Database: chtv.sqlite , Table: match_info
    // TODO: 1. Create table with match_id, over, video_path, sync_status, Set match_id and over as composite primary key
    // TODO: 2. Method to insert match_info with video path, default sync status is 0
    // TODO: 3. Method to get all match info
    // TODO: 4. Method to update sync status (0 > new, 1 > synced on server, 2 > uploading on server, -1 > deleted locally) for match_id and over
    // TODO: 5. Method to get match info according to status, i.e., get all matched which are not synced on server

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table match_info" +
                "( match_id text , " +
                "video_url text, " +
                "sync_status text, " +
                "over text, " +
                "team_a text, " +
                "team_b text, " +
                "batting_team text, " +
                "score text, " +
                "primary key(match_id , over))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS match_info");
        onCreate(sqLiteDatabase);
    }

    public void insertMatchInfo(MatchInfo info) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = info.toContentValues();
        database.insert("match_info", "", cv);
        Log.e("DATABASE",cv.toString());
    }

    public ArrayList<MatchInfo> getAllMatches() {
        return getMatches(null, null);
    }

    public ArrayList<MatchInfo> getMatchesByStatus(String syncStatus) {
        return getMatches("sync_status=?", new String[]{syncStatus});
    }

    public ArrayList<MatchInfo> getMatchesByMatchIdAndOver(String match_Id, String over) {
        return getMatches("match_id=? AND over =?",new String[]{match_Id,over});
    }

    private ArrayList<MatchInfo> getMatches(String selection, String[] selectionArg) {
        ArrayList<MatchInfo> matches = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query("match_info", null, selection, selectionArg, null, null, null);
        while (cursor.moveToNext()) {
            MatchInfo info = MatchInfo.fromCursor(cursor);
            // TODO: Check video file exists or not
            //File file = new File(info.getVideoUrl());
            //if (file.exists()) {
                matches.add(info);
            //}
        }
        cursor.close();
        Log.e("Matches", String.valueOf(matches));
        return matches;
    }
}
