package com.barisetech.www.workmanage.bean.signin;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.barisetech.www.workmanage.bean.ImageInfo;
import com.barisetech.www.workmanage.bean.worktask.TaskSiteBean;

import java.util.List;

/**
 * Created by LJH on 2018/9/20.
 */
@Entity(tableName = "sign_in")
public class ReqSignIn {
    /**
     * Name : ttx
     * SiteId : 1
     * Longitude : 0.1
     * Latitude : 0.2
     * WorkImageList :
     * State :  状态1正常打卡2离线打卡3未打卡4打卡失败
     * SiteState :   站点状态1正常2异常
     */
    @PrimaryKey
    public int id;

    public String Name;
    public String SiteId;
    public String DateTime;
    public String Longitude;
    public String Latitude;
    public String Remark;
    public String State;
    public String SiteState;
    public String UserLongitude;
    public String UserLatitude;

    @Ignore
    public List<ImageInfo> WorkImageList;

    public void toTaskSite(TaskSiteBean siteBean) {
        Name = siteBean.Name;
        SiteId = String.valueOf(siteBean.SiteId);
        DateTime = siteBean.DateTime;
        Longitude = String.valueOf(siteBean.Longitude);
        Latitude = String.valueOf(siteBean.Latitude);
        Remark = siteBean.Remark;
        State = String.valueOf(siteBean.State);
        SiteState = String.valueOf(siteBean.SiteState);
        UserLatitude = String.valueOf(siteBean.UserLatitude);
        UserLongitude = String.valueOf(siteBean.UserLongitude);
        WorkImageList = siteBean.WorkImageList;
    }
}
