package com.afollestad.materialcamerasample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table match_info" +
                "( match_id text , " +
                "over text , " +
                "video_url text, " +
                "sync_status text, " +
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

    public boolean insertData(String videoPath,String match_id, String over_started) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("video_url", videoPath);
        contentValues.put("match_id", match_id);
        contentValues.put("over_started", over_started);
//        contentValues.put("updatedData", videoData);
        sqLiteDatabase.insert("match_info", null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "match_info");
        return numRows;
    }

    public Cursor getData(String videoPath) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from match_info where video_url = '" + videoPath + "'", null);
        return cursor;
    }

    public ArrayList<MatchInfo> getMatchesByStatus(String syncStatus) {
        return getMatches("sync_status=?", new String[]{syncStatus});
    }

    public ArrayList<MatchInfo> getMatchesByMatchIdAndOver(String match_Id, String over)
    {
        return getMatches("match_id=? AND over =?",new String[]{match_Id,over});
    }

    private ArrayList<MatchInfo> getMatches(String selection, String[] selectionArg) {
        ArrayList<MatchInfo> matches = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query("match_info", null, selection, selectionArg, null, null, null);
        while (cursor.moveToNext()) {
            MatchInfo info = MatchInfo.fromCursor(cursor);
            matches.add(info);
        }
        cursor.close();
        Log.e("Matches", String.valueOf(matches));
        return matches;
    }

    public ArrayList<String> getVideoData(String videoPath) {
        ArrayList<String> arrayList1 = new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from match_info where videoPath = '" + videoPath + "'", null);
        cursor.moveToFirst();
        do {
            arrayList1.add(cursor.getString(cursor.getColumnIndex(FILEPATH)));
            arrayList1.add(cursor.getString(cursor.getColumnIndex(MATCH_ID)));
            arrayList1.add(cursor.getString(cursor.getColumnIndex(OVER)));

            Log.e("File", "Db File name    :" + cursor.getString(cursor.getColumnIndex(FILEPATH)));
            Log.e("File", "Db File name    :" + cursor.getString(cursor.getColumnIndex(VIDEODATA)));
        } while (cursor.moveToNext());
        return arrayList1;
    }


    public boolean updateData(String videoPath, String videoData) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FILEPATH, videoPath);
        contentValues.put(VIDEODATA, videoData);
        sqLiteDatabase.update("match_info", contentValues, FILEPATH + " = ?", new String[]{videoPath});
        return true;
    }

    public ArrayList<String> getDataFromTable() {
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from match_info", null);
        cursor.moveToFirst();
        do {
            arrayList.add(cursor.getString(cursor.getColumnIndex(FILEPATH)));
//            arrayList.add(cursor.getString(cursor.getColumnIndex(VIDEODATA)));
            Log.e("File", "Db File name    :" + cursor.getString(cursor.getColumnIndex(VIDEODATA)));
        } while (cursor.moveToNext());
        return arrayList;
    }

    public ArrayList<MatchInfo> getAllMatches() {
        return getMatches(null, null);

    }
}
