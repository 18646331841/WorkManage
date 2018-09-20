package com.barisetech.www.workmanage.bean.worktask;

import com.barisetech.www.workmanage.bean.ImageInfo;

import java.util.List;

/**
 * 专门Add时使用
 * Created by LJH on 2018/9/18.
 */
public class TaskSiteBeanAdd {
    /**
     * Name : ttx
     * SiteId : 1
     * Longitude : 0.1
     * Latitude : 0.2
     * WorkImageList :
     * State :  状态1正常打卡2离线打卡3未打卡4打卡失败
     * SiteState :   站点状态1正常2异常
     */

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
    public List<ImageInfo> WorkImageList;
}
