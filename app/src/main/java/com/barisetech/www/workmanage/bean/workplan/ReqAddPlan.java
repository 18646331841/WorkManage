package com.barisetech.www.workmanage.bean.workplan;

import java.util.List;

/**
 * Created by LJH on 2018/9/18.
 */
public class ReqAddPlan {
    /**
     * Id : 2
     * Name : ss
     * TotalNumberOfTimes : 3
     * TimesOfCompletion : 0
     * Publisher : a
     * PersonLiable : b
     * State : 1
     * BeginTime : 2013-03-03 12:12:12
     * EndTime : 2018-03-02 12:12:12
     * PlanSiteList : [{"SiteId":"2","Name":"55","Longitude":"0","Latitude":"0"},{"SiteId":"2","Name":"55",
     * "Longitude":"0","Latitude":"0"}]
     * isAdd : true
     */

    public String Id;
    public String Name;
    public String TotalNumberOfTimes;
    public String TimesOfCompletion;
    public String Publisher;
    public String PersonLiable;
    public String State;
    public String BeginTime;
    public String EndTime;
    public String isAdd;
    public List<PlanSiteBean> PlanSiteList;
}
