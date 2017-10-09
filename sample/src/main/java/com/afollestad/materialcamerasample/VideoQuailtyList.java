package com.afollestad.materialcamerasample;

/**
 * Created by mukund.jogi on 9/10/17.
 */

class VideoQuailtyList {
    private int video_QualityID;
    private String videoQualtiy;

    public VideoQuailtyList(String selected_Video_quality, int qualityId) {
        videoQualtiy = selected_Video_quality;
        video_QualityID = qualityId;
    }

    public void setVideoQuality(int selected_Quality){
        video_QualityID = selected_Quality;
    }

    public int getVideoQuality() {
        return video_QualityID;
    }

    @Override
    public String toString() {
        return videoQualtiy;
    }
}
