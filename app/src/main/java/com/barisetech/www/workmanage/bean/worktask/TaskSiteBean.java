package com.barisetech.www.workmanage.bean.worktask;

/**
 * Created by LJH on 2018/9/18.
 */
public class TaskSiteBean {
    /**
     * Name : ttx
     * SiteId : 1
     * Longitude : 0.1
     * Latitude : 0.2
     * WorkImageList :
     */

    private String Name;
    private String SiteId;
    private String Longitude;
    private String Latitude;
    private String WorkImageList;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSiteId() {
        return SiteId;
    }

    public void setSiteId(String SiteId) {
        this.SiteId = SiteId;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getWorkImageList() {
        return WorkImageList;
    }

    public void setWorkImageList(String WorkImageList) {
        this.WorkImageList = WorkImageList;
    }
}
