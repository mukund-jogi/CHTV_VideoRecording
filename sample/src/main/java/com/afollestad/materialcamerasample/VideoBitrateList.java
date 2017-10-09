package com.afollestad.materialcamerasample;

/**
 * Created by mukund.jogi on 9/10/17.
 */

class VideoBitrateList{
    private String bitrate;
    private int bitrateId;
    public VideoBitrateList(String selectedBitrate, int selectedBitrateID) {
        bitrate = selectedBitrate;
        bitrateId = selectedBitrateID;
    }

    public void setBitrate(int selected_BitrateID){
        bitrateId = selected_BitrateID;
    }

    public int getVideoBitrate() {
        return bitrateId;
    }

    @Override
    public String toString() {
        return bitrate;
    }
}
