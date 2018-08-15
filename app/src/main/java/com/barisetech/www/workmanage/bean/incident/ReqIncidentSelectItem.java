package com.barisetech.www.workmanage.bean.incident;

/**
 * Created by LJH on 2018/8/15.
 */
public class ReqIncidentSelectItem {
    /**
     * mStartTime : 2011-01-01 12:12:12
     * mEndTime : 2019-01-01 12:12:12
     * TimeQueryChecked : true
     * mIncidentType : 1
     * siteIdQueryChecked : false
     * siteID : 0
     */

    private String mStartTime;
    private String mEndTime;
    private String TimeQueryChecked;
    private String mIncidentType;
    private String siteIdQueryChecked;
    private String siteID;

    public String getMStartTime() {
        return mStartTime;
    }

    public void setMStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getMEndTime() {
        return mEndTime;
    }

    public void setMEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getTimeQueryChecked() {
        return TimeQueryChecked;
    }

    public void setTimeQueryChecked(String TimeQueryChecked) {
        this.TimeQueryChecked = TimeQueryChecked;
    }

    public String getMIncidentType() {
        return mIncidentType;
    }

    public void setMIncidentType(String mIncidentType) {
        this.mIncidentType = mIncidentType;
    }

    public String getSiteIdQueryChecked() {
        return siteIdQueryChecked;
    }

    public void setSiteIdQueryChecked(String siteIdQueryChecked) {
        this.siteIdQueryChecked = siteIdQueryChecked;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
}
