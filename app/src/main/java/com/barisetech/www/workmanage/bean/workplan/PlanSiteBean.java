package com.barisetech.www.workmanage.bean.workplan;

import com.barisetech.www.workmanage.bean.site.SiteBean;

import java.io.Serializable;

/**
 * Created by LJH on 2018/9/18.
 */
public class PlanSiteBean implements Serializable{
    /**
     * SiteId : 2
     * Name : 55
     * Longitude : 0
     * Latitude : 0
     */

    public String SiteId;
    public String Name;
    public String Longitude;
    public String Latitude;

    public void toSite(SiteBean siteBean) {
        SiteId = String.valueOf(siteBean.SiteId);
        Name = siteBean.Name;
        Longitude = String.valueOf(siteBean.Longitude);
        Latitude = String.valueOf(siteBean.Latitude);
    }
}
