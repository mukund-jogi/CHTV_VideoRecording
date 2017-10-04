package com.afollestad.materialcamerasample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mukund.jogi on 3/10/17.
 */

class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "VideoListProvider.db";
    public static final String FILEPATH = "videoPath";
    public static final String VIDEODATA = "updatedData";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table VideoListProvider" + "(videoPath text primary key, updatedData text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS VideoListProvider");
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String videoPath, String videoData) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("videoPath", videoPath);
        contentValues.put("updatedData", videoData);
        sqLiteDatabase.insert("VideoListProvider", null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "VideoListProvider");
        return numRows;
    }

    public Cursor getData(String videoPath) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from VideoListProvider where videoPath = '" + videoPath + "'", null);
        return cursor;
    }

    public String getVideoData(String videoPath) {
//        ArrayList<String> arrayList1 = new ArrayList<String>();
        String videoDATA;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from VideoListProvider where videoPath = '" + videoPath + "'", null);
        cursor.moveToFirst();
        do {
//            arrayList1.add(cursor.getString(cursor.getColumnIndex(VIDEODATA)));
            videoDATA = cursor.getString(cursor.getColumnIndex(VIDEODATA));
            Log.e("File", "Db File name    :" + cursor.getString(cursor.getColumnIndex(FILEPATH)));
            Log.e("File", "Db File name    :" + cursor.getString(cursor.getColumnIndex(VIDEODATA)));
        } while (cursor.moveToNext());
        return videoDATA;
    }


    public boolean updateData(String videoPath, String videoData) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FILEPATH, videoPath);
        contentValues.put(VIDEODATA, videoData);
        sqLiteDatabase.update("VideoListProvider", contentValues, FILEPATH + " = ?", new String[]{videoPath});
        return true;
    }

    public ArrayList<String> getDataFromTable() {
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from VideoListProvider", null);
        cursor.moveToFirst();
        do {
//            arrayList.add(cursor.getString(cursor.getColumnIndex(FILEPATH)));
            arrayList.add(cursor.getString(cursor.getColumnIndex(VIDEODATA)));
            Log.e("File", "Db File name    :" + cursor.getString(cursor.getColumnIndex(VIDEODATA)));
        } while (cursor.moveToNext());
        return arrayList;
    }
}
