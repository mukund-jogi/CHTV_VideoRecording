package com.afollestad.materialcamerasample;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by mukund.jogi on 5/10/17.
 */

public class MatchInfo {
    private String matchId;
    private String over;
    private String videoUrl;
    private String syncStatus;
    private String teamAName;
    private String teamBName;
    private String score;
    private String battingTeam;

    public MatchInfo() {
    }

    public MatchInfo(String matchId, String over, String videoUrl, String syncStatus, String teamA, String teamB, String score, String battingTeam) {
        this.matchId = matchId;
        this.over = over;
        this.videoUrl = videoUrl;
        this.syncStatus = syncStatus;
        this.teamAName = teamA;
        this.teamBName = teamB;
        this.score = score;
        this.battingTeam = battingTeam;
    }

    public static MatchInfo fromCursor(Cursor cursor) {
        MatchInfo info = new MatchInfo();
        info.matchId = cursor.getString(cursor.getColumnIndex("match_id"));
        info.over = cursor.getString(cursor.getColumnIndex("over"));
        info.videoUrl = cursor.getString(cursor.getColumnIndex("video_url"));
        info.syncStatus = cursor.getString(cursor.getColumnIndex("sync_status"));
        info.teamAName = cursor.getString(cursor.getColumnIndex("team_a"));
        info.teamBName = cursor.getString(cursor.getColumnIndex("team_b"));
        info.score = cursor.getString(cursor.getColumnIndex("score"));
        info.battingTeam = cursor.getString(cursor.getColumnIndex("batting_team"));
        return info;
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("match_id", matchId);
        cv.put("over", over);
        cv.put("video_url", videoUrl);
        cv.put("sync_status", syncStatus);
        cv.put("team_a",teamAName);
        cv.put("team_b",teamBName);
        cv.put("score",score);
        cv.put("batting_team",battingTeam);
        return cv;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getTeamAName() {return teamAName;}

    public void setTeamAName(String teamAName) {this.teamAName = teamAName;}

    public String getTeamBName() {return teamBName;}

    public void setTeamBName(String teamBName) {this.teamBName = teamBName;}

    public String getScore() {return score;}

    public void setScore(String score) {this.score = score;}

    public String getBattingTeam() {return battingTeam;}

    public void setBattingTeam(String battingTeam) {this.battingTeam = battingTeam;}

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Match_id: %s Teams: %s _vs_ %s \nBattingTeam: %s -  Score: %s \nOver: %s \nVideo_url: %s \nSync_status: %s", matchId,teamAName,teamBName,battingTeam,score, over, videoUrl, syncStatus);
//        return String.format(Locale.getDefault(), "Match_id: %s TeamA: %s vs TeamB: %s \nBattingTeam: %s -  Score: %s \nOver: %s \nVideo_url: %s \nSync_status: %s", matchId,teamAName,teamBName,battingTeam,score, over, videoUrl, syncStatus);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public static MatchInfo fromJson(JSONObject jsonMatchInfo) {
        // {"match_id":17316,"team_a":"Team Rogger","team_b":"Dev5","score":"9\/0","over":"3","batting_team":"Team Rogger","command":"START_OVER"}
        MatchInfo info = new MatchInfo();
        info.matchId = jsonMatchInfo.optString("match_id", "0");
        info.teamAName = jsonMatchInfo.optString("team_a", "0");
        info.teamBName = jsonMatchInfo.optString("team_b", "0");
        info.battingTeam = jsonMatchInfo.optString("batting_team","0");
        info.score = jsonMatchInfo.optString("score","0");
        info.over = jsonMatchInfo.optString("over", "0");
        info.videoUrl = "";
        info.syncStatus = "Local";
        return info;
    }
}
